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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrapingService {
    private final ScrapRepository scrapRepository;

    @PostConstruct
    public void startScraping() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    List<Scrap> scraps = ScrapSaram();
                    processScrapResults(scraps);
                } catch (IOException e) {
                    log.error("에러");
                }

            }
        };
        // crawl 주기 설정 (15분 마다)
        long delay = 0; // 딜레이 설정
        long period = 60 * 15 * 1000;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, delay, period);
    }


    public List<Scrap> ScrapSaram() throws IOException {
        List<Scrap> scraps = new ArrayList<>();

        // 기본 url 설정
        String url = "https://www.saramin.co.kr/zf_user/search/recruit?search_area=main&search_done=y&search_optional_item=n&searchType=recently&searchword=%EA%B0%9C%EB%B0%9C%EC%9E%90&recruitPage=1&recruitSort=relation&recruitPageCount=30&inner_com_type=&company_cd=0%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C9%2C10&show_applied=&quick_apply=&except_read=&ai_head_hunting=";
        String articleUrlPrefix = "https://www.saramin.co.kr";


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
                    .header("Referer", "https://www.saramin.co.kr/zf_user/jobs/relay/view?isMypage=no&rec_idx=45868854&recommend_ids=eJxVj8ERAzEIA6vJHwwS4p1Crv8u4lzmbOe5s8KyEkKz6tLwV70TGhwZl%2FyHVYJhoUhjznDfyHS6PTbnrQPLqgKxkU12HU9BoYUot7bd294YO4ygHdYsv994bFVIf9i9w6Nn2WF99B44d8e518Q8ijylu%2BgDiSFAdg%3D%3D&view_type=avatar&gz=1&t_ref_scnid=805&t_ref_content=SRI_100_MAIN_AI_RCT_LOGOUT&t_ref=main&t_category=relay_view&t_content=relay_view_avatar#seq=0")
                    .build();
            Response response = client.newCall(request).execute();
            String html = response.body().string();

            // Jsoup을 사용하여 HTML 파싱
            doc = Jsoup.parse(html);
        } catch (Exception e) {
            e.printStackTrace();

        }

        Elements elements = doc.select("#recruit_info_list > div.content > div");
        int count = elements.size(); // div.item_recruit의 개수
        System.out.println("div.item_recruit 개수: " + count);
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
            String articleText = element.select("div.area_job > h2 > a > span").text();
            String articleUrl = element.select("div.area_job > h2 > a").attr("href");
            String company = element.select("div.area_corp > strong > a").text();
            String deadline = element.select("div.area_job > div.job_date > span.date").text();
            String location = element.select("div.job_condition > span > a").text();
            String experience = element.select("div.job_condition > span:nth-child(2)").text();
            String requirement = element.select("div.job_condition > span:nth-child(3)").text();
            String jobType = element.select("div.job_condition > span:nth-child(4)").text();


            articleUrl = articleUrlPrefix + articleUrl;

            // articleUrl에서 rec_idx 값을 추출
            // 사람인 공고의 키값
            String recIdx = "";
            int recIdxStartIndex = articleUrl.indexOf("rec_idx=");
            if (recIdxStartIndex != -1) {
                int recIdxEndIndex = articleUrl.indexOf("&", recIdxStartIndex);
                if (recIdxEndIndex != -1) {
                    recIdx = articleUrl.substring(recIdxStartIndex + 8, recIdxEndIndex);

                } else {

                    recIdx = articleUrl.substring(recIdxStartIndex + 8);
                }
            }

            //rec_idx값이 이미 db에 저장된게 있는지 확인
            boolean isDuplicate = false;
            List<Scrap> existingScraps = scrapRepository.findAll();
            for (Scrap existingScrap : existingScraps) {
                String existingArticleUrl = existingScrap.getArticleUrl();
                int existingRecIdxStartIndex = existingArticleUrl.indexOf("rec_idx=");
                if (existingRecIdxStartIndex != -1) {
                    int existingRecIdxEndIndex = existingArticleUrl.indexOf("&", existingRecIdxStartIndex);
                    if (existingRecIdxEndIndex != -1) {
                        String existingRecIdx = existingArticleUrl.substring(existingRecIdxStartIndex + 8, existingRecIdxEndIndex);
                        if (existingRecIdx.equals(recIdx)) {
                            isDuplicate = true;
                            break;
                        }
                    }
                }
            }
            Scrap scrap = new Scrap(null, articleText, articleUrl, company, deadline, location, experience, requirement, jobType, false);
            scraps.add(scrap);
            // 제외
            if (!isDuplicate) {
                scrapRepository.save(scrap);
            }
        }

        return scraps;
    }

    public void processScrapResults(List<Scrap> scraps) {
        for (Scrap scrap : scraps) {
            System.out.println("aritlceText: " + scrap.getArticleText());
            System.out.println("--------------------------------------");
        }
    }


}

