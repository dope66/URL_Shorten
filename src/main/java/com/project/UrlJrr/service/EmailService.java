package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserService userService;
    private final JavaMailSender javaMailSender;
    private final ScrapingService scrapingService;
    private final ScrapRepository scrapRepository;

    private final UrlMappingService urlMappingService;

    //    @Scheduled(initialDelay = 3000, fixedRate = 300000) // 5분마다 확인용
    @Scheduled(cron = "0 0 15 * * ?") // 매일 오후 3시에
    public void sendEmailsToSubscribers() throws IOException {
        System.out.println("이메일 서비스 시작");
        // 크롤링 데이터 가져오기
        List<Scrap> scraps = scrapingService.ScrapSaram();
        // 발송하지 않은 정보 가져오기
        List<Scrap> sentScraps = scrapRepository.findBySent(false);

        System.out.println("보낼 공고 갯수 =" + sentScraps.size());
        // 이메일 본문 쓰기
        StringBuilder emailContent = new StringBuilder();
        for (Scrap scrap : sentScraps) {
            String shortUrl = urlMappingService.generateShortUrl(scrap.getArticleUrl());
            System.out.println(shortUrl);

            emailContent.append("회사: ").append(scrap.getCompany()).append("\n")
                    .append("제목: ").append(scrap.getArticleText()).append("\n")
                    .append("URL: ").append(scrap.getArticleUrl()).append("\n\n");
            // 더 추가 가능 표로 보기 쉽게 변경 해보자
        }
        /*
        to = 현재는 개인 이메일, 후에 회원 이메일로 변경 예정
        subject = 이메일 제목
        text = 내용
        * */
        // 회원가입된 사용자들의 이메일 가져오기
        List<String> subscriberEmails = userService.getAllUserEmails();
        String subject = "새로운 채용 정보 알림";
        String text = emailContent.toString();
        // 이메일이 저장된 사용자가 있는 경우에만 발송
        if (!subscriberEmails.isEmpty()) {
            // 각 사용자에게 이메일 발송
            for (String email : subscriberEmails) {
                sendEmail(email, subject, text);
            }
        } else {
            System.out.println("이메일이 저장된 사용자가 없습니다.");
        }
        // 이메일을 발송한 scrap들의 sent 값을 true로 변경하여 중복 발송 방지
        for (Scrap scrap : sentScraps) {
            scrap.setSent(true);
            scrapRepository.save(scrap);
        }

        System.out.println("실행 종료");
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}
