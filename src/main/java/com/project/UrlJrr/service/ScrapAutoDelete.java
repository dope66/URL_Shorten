package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ScrapAutoDelete {

    private final ScrapRepository scrapRepository;


    @Scheduled(initialDelay = 3000, fixedRate = 3600000) // 1시간
    public void deleteExpiredScraps() {
        System.out.println("----------------------");
        System.out.println("DB 삭제 실행");
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Scrap> scraps = scrapRepository.findAll();
        LocalDateTime deadlineDateTime = null;
        for (Scrap scrap : scraps) {
            String deadline = scrap.getDeadline();
            String creatDate = scrap.getCreateDate();
            Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
            Matcher matcher = pattern.matcher(deadline);
            if (deadline.equals("채용시") || deadline.equals("상시채용")) {

                int year = Integer.parseInt(creatDate.substring(0, 4));
                int month = Integer.parseInt(creatDate.substring(5, 7));
                int day = Integer.parseInt(creatDate.substring(8, 10));

                LocalDateTime creationDateTime = LocalDateTime.of(year, month, day, 0, 0, 0);
                LocalDateTime deletionDateTime = creationDateTime.plusMonths(1);
                if (currentDateTime.isAfter(deletionDateTime)) {
                    scrapRepository.delete(scrap);
                    System.out.println("Scrap 삭제됨: " + scrap.getId());
                }


            } else {
                //           날짜 형식인 경우
                if (matcher.matches()) {
                    int year = Integer.parseInt(deadline.substring(0, 4));
                    int month = Integer.parseInt(deadline.substring(5, 7));
                    int day = Integer.parseInt(deadline.substring(8, 10));

                    // Scrap의 날짜 정보로 LocalDateTime 생성
                    deadlineDateTime = LocalDateTime.of(year, month, day, 23, 59, 59);

                    if (currentDateTime.isAfter(deadlineDateTime)) {
                        scrapRepository.delete(scrap);
                        System.out.println("Scrap 삭제됨: " + scrap.getId());
                    }
                }


            }
        }
        System.out.println("DB 삭제 서비스 끝");
    }


}
