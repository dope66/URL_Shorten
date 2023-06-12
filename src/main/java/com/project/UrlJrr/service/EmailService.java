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
    private final JavaMailSender javaMailSender;
    private final ScrapingService scrapingService;
    private final ScrapRepository scrapRepository;

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
        String to = "prince628@naver.com";
        String subject = "새로운 채용 정보 알림";
        String text = emailContent.toString();
        sendEmail(to, subject, text);
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
