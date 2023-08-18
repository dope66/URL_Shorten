package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.MatchingResultDto;
import com.project.UrlJrr.entity.Email;
import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.repository.EmailRepository;
import com.project.UrlJrr.repository.ScrapRepository;
import com.project.UrlJrr.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private final ScrapRepository scrapRepository;
    private final TaskScheduler taskScheduler;
    private final EmailRepository emailRepository;
    private final MatchingService matchingService;
    private final UserRepository userRepository;


    @Value("${server.name}")
    private String serverName;
    //서버 재시작 시 설정할 기본 스캐줄
    @Getter
    private String emailSchedule = "0 0 18 * * ?";


    private ScheduledFuture<?> scheduledTask;

    public void configureTasks() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true);
        }

        Runnable task = () -> {
            try {
                sendEmailsToSubscribers();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        Trigger trigger = new CronTrigger(emailSchedule);
        scheduledTask = taskScheduler.schedule(task, trigger);
    }

    // 스케줄링 변경 메서드
    public void updateEmailSchedule(String newSchedule) {
        emailSchedule = newSchedule;
        System.out.println("변견된 스캐줄 : " + newSchedule);
        configureTasks();

    }


    @Scheduled(cron = "#{emailService.getEmailSchedule()}")
    public void sendEmailsToSubscribers() throws IOException {
        System.out.println("이메일 서비스 시작");
        // 발송하지 않은 정보 가져오기
        List<Scrap> unsentScraps = scrapRepository.findBySent(false);
        System.out.println("보낼 공고 갯수 =" + unsentScraps.size());
        if (unsentScraps.isEmpty()) {
            System.out.println("보낼 공고가 없습니다.");
            return; // 이메일을 발송할 공고가 없으면 메서드 종료
        }




        List<String> subscriberEmails = userService.getSubScribeEmail();
        for(String email : subscriberEmails){
            Optional<User> userOptional = userRepository.findByEmail(email); // 이메일을 기반으로 사용자 정보 조회
            if(userOptional.isPresent()){
                User user = userOptional.get();
                String emailContent = buildEmailContent(unsentScraps, user);
                String subject = LocalDate.now().format(DateTimeFormatter.ofPattern("MM월 dd일")) + "채용 정보 알림 " + unsentScraps.size() + " 개의 공고";

                sendEmailsToSubscribers(subscriberEmails, subject, emailContent);
            }

        }



        System.out.println("이메일 서비스 실행 종료");
    }

    // 이메일 작성
    public String buildEmailContent(List<Scrap> scraps, User user){
        StringBuilder emailContent = new StringBuilder();
        int maxSkillStacklength=40; // 최대 길이
        List<MatchingResultDto> matchingResults = matchingService.calculateAndStoreMatchingResults(user, scraps);

        for (int i = 0; i < scraps.size(); i++) {
            Scrap scrap = scraps.get(i);
            MatchingResultDto matchingResultDto = matchingResults.get(i);

            String skillStack = scrap.getSkillStack();
            if(skillStack.length() > maxSkillStacklength){
                skillStack = skillStack.substring(0, maxSkillStacklength) + "...";
            }

            emailContent.append("회사: ").append(scrap.getCompany()).append("\n")
                    .append("제목: ").append(scrap.getArticleText()).append("\n")
                    .append("요구 기술 스택: ").append(skillStack).append("\n")
                    .append("요구 경력 : ").append(scrap.getExperience()).append("\n")
                    .append("나와의 매칭 등급: ").append(matchingResultDto.getMatchingGrade()).append("\n")
                    .append("URL: ").append(serverName).append("/matching/").append(scrap.getId()).append("\n\n");
        }
        return emailContent.toString();
    }

    // 구독자에게만 이메일 전송
    public void sendEmailsToSubscribers(List<String> subscriberEmails,String subject,String text){
        if (!subscriberEmails.isEmpty()) {
            // 각 사용자에게 이메일 발송
            subscriberEmails.forEach(email -> sendEmail(email, subject, text));
            // 이메일 데이터 저장
            saveSentEmailData(subscriberEmails, subject);
            // 이메일 발송 처리된 scrap들의 sent 값을 true로 변경하여 중복 발송 방지
            markScrapsAsSent();
        } else {
            System.out.println("저장된 이메일이 없습니다.");
        }

        System.out.println("이메일을 성공적으로 보냈습니다.");
    }
    // 전송
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

    }
    // 전송한 이메일 로그로 저장
    private void saveSentEmailData(List<String> subscriberEmails, String subject) {
        String recipients = String.join(", ", subscriberEmails);

        Email email = Email.builder()
                .recipient(recipients)
                .subject(subject)
                .sendEmailTime(LocalDateTime.now())
                .build();
        emailRepository.save(email);
    }
    // 중복으로 이메일을 보내는것을 방지하기 위해서 sent값 true로 변경
    private void markScrapsAsSent() {
        List<Scrap> sentScraps = scrapRepository.findBySent(false);
        sentScraps.forEach(scrap -> {
            scrap.setSent(true);
            scrapRepository.save(scrap);
        });
    }
    public Page<Email> emailList(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return emailRepository.findAll(pageable);
    }


}
