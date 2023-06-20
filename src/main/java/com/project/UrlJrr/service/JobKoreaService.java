package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobKoreaService {

    private final ScrapRepository scrapRepository;

    @PostConstruct
    public void startScraping() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                List<Scrap> scraps = jobkoreaScrap();

            }
        };
        // crawling 주기 설정 (20분 마다)
        long delay = 0; // 딜레이 설정
        long period = 60 * 20 * 1000;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, delay, period);
    }

    public List<Scrap> jobkoreaScrap() {
        System.out.println("잡코리아 스크랩 시작");
        List<Scrap> scraps = new ArrayList<>();

        // 기본 url 설정
        String url = "https://www.jobkorea.co.kr/Search/?stext=%EA%B0%9C%EB%B0%9C%EC%9E%90&tabType=recruit&Page_No=1";
        String articleUrlPrefix = "https://www.jobkorea.co.kr/";
        // Jsoup을 이용한 연결 설정 http 2.0 설정 코드
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(List.of(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
                .build();
        Document doc = null;
        try {
            // OkHttp를 사용하여 HTTP/2.0 통신
            Request request = new Request.Builder()
                    .url(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
                    .header("Referer", "https://www.jobkorea.co.kr/Recruit/GI_Read/42052468?Oem_Code=C1&logpath=1&stext=%EA%B0%9C%EB%B0%9C%EC%9E%90&listno=1")
                    .build();
            Response response = client.newCall(request).execute();
            String html = response.body().string();

            // Jsoup을 사용하여 HTML 파싱
            doc = Jsoup.parse(html);
        } catch (Exception e) {
            e.printStackTrace();

        }

        Elements elements = doc.select("#content > div > div > div.cnt-list-wrap > div >" +
                " div.recruit-info > div.lists > div > div.list-default > ul > li");
        int count = elements.size();
        System.out.println("JObKorea 갯수 : " + count);
        /*
         * articleText  : 공고 제목
         * articleUrl : 공고 링크
         * company : 회사
         * deadline : 공고 게시 기간
         * location : 지역
         * experience : 경력
         * requirement : 학력 요구사항
         * jobType : 정규직 여부(?)
         * */
        for (Element element : elements) {
            String articleText = element.select("div.post-list-info > a.title dev_view on ").text();
            String articleUrl = element.select("div.post-list-info > a").attr("href");
            Elements skillStackElements = element.select("div.post-list-info > p.etc");
            StringJoiner skillStackJoiner = new StringJoiner(", ");
            for (Element skillStackElement : skillStackElements) {
                String skillStack = skillStackElement.text();
                skillStackJoiner.add(skillStack);
            }
            String skillStack = skillStackJoiner.toString();
            String company = element.select("div.post-list-corp > a.name dev_view ").text();
            String deadline = element.select("div.post-list-info > p.option > span.date").text();
            String location = element.select("div.post-list-info > p.option > span.loc long").text();
            String experience = element.select("div.post-list-info > p.option > span.exp").text();
            String requirement = element.select("div.post-list-info > p.option > span.edu").text();
            String jobType = element.select("div.post-list-info > p.option > span:nth-child(3)").text();

            articleUrl = articleUrlPrefix + articleUrl;

            String urlKey = element.select("li.list-post").attr("data-gno");
            System.out.println("JOb korea urlKey " + urlKey);

            boolean isDuplicate = false;
            List<Scrap> existingScraps = scrapRepository.findAll();
            for (Scrap existingScrap : existingScraps) {
                String existingArticleUrl = existingScrap.getArticleUrl();
                int existingStartUrlKey = existingArticleUrl.indexOf("data-gno");
                if (existingStartUrlKey == -1) {
                    int existingEndUrlKey = existingArticleUrl.indexOf("?", existingStartUrlKey);
                    if (existingEndUrlKey != -1) {
                        String existingUrlKey = existingArticleUrl.substring(existingStartUrlKey + 8, existingEndUrlKey);
                        if (existingUrlKey.equals(urlKey)) {
                            isDuplicate = true;
                            break;
                        }
                    }
                }
            }
            Scrap scrap = Scrap.builder()
                    .articleText(articleText)
                    .articleUrl(articleUrl)
                    .skillStack(skillStack)
                    .company(company)
                    .deadline(deadline)
                    .location(location)
                    .experience(experience)
                    .requirement(requirement)
                    .jobType(jobType)
                    .sent(false)
                    .createDate(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))))
                    .build();
            scraps.add(scrap);

            if(!isDuplicate){
                scrapRepository.save(scrap);
            }
        }


        return scraps;
    }


}