package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
@RequiredArgsConstructor
public class ScrapAutoDelete {

    private final ScrapRepository scrapRepository;


    @Scheduled(initialDelay = 3000, fixedRate = 300000) // 5분마다 확인용
    public void deleteExpiredScraps() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Scrap> scraps = scrapRepository.findAll();

        for (Scrap scrap : scraps) {
            String deadline = scrap.getDeadline();
            LocalDateTime deadlineDateTime;

            if (deadline.equals("오늘마감")) {

                deadlineDateTime = currentDateTime;

            }
            else if (deadline.equals("내일마감")) {
                // 현재 날짜의 다음 날의 마지막 시간으로 설정
                deadlineDateTime = LocalDateTime.of(currentDateTime.toLocalDate().plusDays(1), LocalTime.MAX);

            }
            else {
//                날짜 형식인 경우
                Pattern pattern = Pattern.compile("~ (\\d{2})/(\\d{2})");
                Matcher matcher = pattern.matcher(deadline);

                if (matcher.find()) {
                    int month = Integer.parseInt(matcher.group(1));
                    int day = Integer.parseInt(matcher.group(2));
                    // Scrap의 날짜 정보로 LocalDateTime 생성
                    deadlineDateTime = LocalDateTime.of(currentDateTime.getYear(), month, day, 23, 59, 59);
                } else {
                    continue; // 날짜 형식이 아니면 다음 Scrap으로 넘어감
                }
            }

            if (currentDateTime.isAfter(deadlineDateTime)) {
                scrapRepository.delete(scrap);
                System.out.println("Scrap 삭제됨: " + scrap.getId());
            }
        }
    }
}
