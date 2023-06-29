package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private final ScrapingService scrapingService;
    private final ScrapRepository scrapRepository;
    private final UrlMappingService urlMappingService;
    private final TaskScheduler taskScheduler;

    @Value("${server.name}") //https://prince.pigworld.dev
    private String serverName;

    private String emailSchedule = "0 5 18 * * ?";



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

        // 이후에 스케줄링이 자동으로 업데이트됩니다.
    }
    public String getEmailSchedule() {
        return emailSchedule;
    }


    //    @Scheduled(initialDelay = 3000, fixedRate = 300000) // 5분마다 확인용
//    @Scheduled(cron = "0 0 15 * * ?") // 매일 오후 3시에
    @Scheduled(cron = "#{emailService.getEmailSchedule()}")
    public void sendEmailsToSubscribers() throws IOException {
        System.out.println("이메일 서비스 시작");
        // 크롤링 데이터 가져오기
        List<Scrap> scraps = scrapingService.ScrapSaram();
        // 발송하지 않은 정보 가져오기
        List<Scrap> sentScraps = scrapRepository.findBySent(false);

        System.out.println("보낼 공고 갯수 =" + sentScraps.size());

        if (sentScraps.isEmpty()) {
            System.out.println("보낼 공고가 없습니다.");
            return; // 이메일을 발송할 공고가 없으면 메서드 종료
        }
        // 이메일 본문 쓰기
        StringBuilder emailContent = new StringBuilder();

        for (Scrap scrap : sentScraps) {
//            final String serverName= "http://localhost:8080";확인용
            emailContent.append("회사: ").append(scrap.getCompany()).append("\n")
                    .append("제목: ").append(scrap.getArticleText()).append("\n")
                    .append("URL: ").append(serverName).append("/matching/").append(scrap.getId()).append("\n\n");
            // 더 추가 가능 표로 보기 쉽게 변경 해보자
        }
        /*
        to = 회원 이메일
        subject = 이메일 제목
        text = 내용
        * */
        // 회원가입된 사용자들의 이메일 가져오기
        List<String> subscriberEmails = userService.getAllUserEmails();
        String subject = LocalDate.now().format(DateTimeFormatter.ofPattern("MM월 dd일")) +"채용 정보 알림";
        String text = emailContent.toString();
        // 이메일이 저장된 사용자가 있는 경우에만 발송
        if (!subscriberEmails.isEmpty()) {
            // 각 사용자에게 이메일 발송
            for (String email : subscriberEmails) {
                sendEmail(email, subject, text);
            }
            System.out.println("이메일을 성공적으로 보냈습니다.");
            // 이메일을 발송한 scrap들의 sent 값을 true로 변경하여 중복 발송 방지
            for (Scrap scrap : sentScraps) {
                scrap.setSent(true);
                scrapRepository.save(scrap);
            }
            System.out.println("sent 값을 true로 변경 ");
        } else {
            System.out.println("저장된 이메일이 없습니다.");
        }
        System.out.println("이메일 서비스 실행 종료");
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }


}
