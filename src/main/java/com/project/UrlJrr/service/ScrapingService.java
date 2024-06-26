package com.project.UrlJrr.service;


<<<<<<< HEAD
import com.project.UrlJrr.domain.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
=======
import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import jakarta.annotation.PostConstruct;
>>>>>>> 51a2909ee3daae4d46c9e325020b144458a80f17
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrapingService{
    private final ScrapRepository scrapRepository;

<<<<<<< HEAD
=======
    @PostConstruct
    public void startScraping() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    List<Scrap> scraps = scrapSaram();

                } catch (IOException e) {
                    log.error("에러");
                }

            }
        };
        // crawling 주기 설정 (15분 마다)
        long delay = 5000; // 딜레이 설정
        long period = 60 * 15 * 1000;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, delay, period);
    }

<<<<<<< HEAD

>>>>>>> 51a2909ee3daae4d46c9e325020b144458a80f17
    public List<Scrap> ScrapSaram() throws IOException {
=======
    public List<Scrap> scrapSaram() throws IOException {
>>>>>>> main
        List<Scrap> scraps = new ArrayList<>();

        // 기본 url 설정
        String url = "https://www.saramin.co.kr/zf_user/search/recruit?search_area=main&search_done=y&search_optional_item=n&searchType=recently&searchword=%EA%B0%9C%EB%B0%9C%EC%9E%90&recruitPage=1&recruitSort=relation&recruitPageCount=100&inner_com_type=&company_cd=0%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C9%2C10&show_applied=&quick_apply=&except_read=&ai_head_hunting=";
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

<<<<<<< HEAD
        for (Element element : elements) {
            String articleText = element.select("div.area_job > h2.job_tit").text();
            String articleUrl = element.select("div.area_job > h2.job_tit > a").attr("href");
            String company = element.select("div.area_corp > strong.corp_name > a").text();
            articleUrl = articleUrlPrefix + articleUrl;

//          이미 존재하는 공고인지 확인
            if (!scrapRepository.existsByArticleUrl(articleUrl)) {
                Scrap scrap = new Scrap(null, articleText, articleUrl, company);
                System.out.println("제목 : " + articleText);
                System.out.println("링크 : " + articleUrl);
                System.out.println("회사 : " + company);
                System.out.println("=================================");
                scraps.add(scrap);
                scrapRepository.save(scrap);

            }

        }

        return scraps;
    }

=======
        }

        Elements elements = doc.select("#recruit_info_list > div.content > div");
        int count = elements.size(); // div.item_recruit의 개수
        System.out.println("사람인 개수: " + count);
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
            Elements skillStackElements = element.select("div.job_sector > a");
            StringJoiner skillStackJoiner = new StringJoiner(", ");
            for (Element skillStackElement : skillStackElements) {
                String skillStack = skillStackElement.text();
                skillStackJoiner.add(skillStack);
            }
            String skillStack = skillStackJoiner.toString();
            String company = element.select("div.area_corp > strong > a").text();
            String deadlineText;
            if (element.select("div.area_job > div.job_date > span > span").size() > 0) {
                deadlineText = element.select("div.area_job > div.job_date > span > span").text();
            } else {
                deadlineText = element.select("div.area_job > div.job_date > span").text();
            }
            String deadline = null;
            switch (deadlineText) {
                case "상시채용", "채용시", "진행예정" -> deadline = deadlineText; // 날짜 형식 변환 없이 그대로 사용
                case "오늘마감" -> deadline = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                case "내일마감" ->
                        deadline = LocalDateTime.now().toLocalDate().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                default -> {
                    // 날짜 변환
                    if (!deadlineText.endsWith("시마감")) {
                        String[] parts = deadlineText.split("[(/]"); // "/" 또는 "("을 기준으로 문자열 분리
                        String month = parts[0].replace("~", "").trim(); // 월 정보 추출 후 공백 제거
                        String day = parts[1].replace(")", "").trim(); // 일 정보 추출 후 공백 제거

                        // 현재 연도를 가져오는 로직
                        int currentYear = LocalDate.now().getYear();
                        // 날짜 문자열 생성
                        String dateString = currentYear + "-" + month + "-" + day;
                        // 생성된 날짜 문자열을 원하는 형식으로 변환
                        LocalDate deadlineDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        deadline = deadlineDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    }
                }
            }


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

            String sourceName = ""; // 기본값 설정
            if (articleUrl.contains("saramin")) {
                sourceName = "사람인";
            } else if (articleUrl.contains("jobkorea")) {
                sourceName = "잡코리아";
            }

            Scrap scrap = Scrap.builder()
                    .articleText(articleText)
                    .articleUrl(articleUrl)
                    .skillStack(skillStack)
                    .sourceSite(sourceName)
                    .company(company)
                    .deadline(deadline)
                    .location(location)
                    .experience(experience)
                    .requirement(requirement)
                    .jobType(jobType)
                    .sent(false)
                    .createDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .build();

            scraps.add(scrap);
            if (!isDuplicate) {
                scrapRepository.save(scrap);
                System.out.println("get Id " + scrap.getId());
            }
        }

        return scraps;
    }

//    public List<Scrap> scrapList() {
//        return scrapRepository.findAll();
//    }
//    public int calculateUnsentCount(List<Scrap> allScraps) {
//        int unsentCount = 0;
//        for (Scrap scrap : allScraps) {
//            if (!scrap.isSent()) {
//                unsentCount++;
//            }
//        }
//        return unsentCount;
//    }

//    public long findMaxId(List<Scrap> allScraps) {
//        long maxId = 0;
//        for (Scrap scrap : allScraps) {
//            if (scrap.getId() > maxId) {
//                maxId = scrap.getId();
//            }
//        }
//        return maxId;
//    }

//    public int calculateDeletedCount(long maxId, int scrapTableSize) {
//        return (int) (maxId - scrapTableSize);
//    }

>>>>>>> 51a2909ee3daae4d46c9e325020b144458a80f17

    public Page<Scrap> findAll(Pageable pageable) {
        return scrapRepository.findAll(pageable);
    }

    public long getTotalScrapCount() {
        return scrapRepository.count(); // Scrap 테이블의 총 레코드 수를 반환
    }

    public Page<Scrap> findByTitleContaining(String title, Pageable pageable) {

        return scrapRepository.findByArticleTextContaining(title, pageable);
    }
}

