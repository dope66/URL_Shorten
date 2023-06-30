package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ScrapAutoDelete {

    private final ScrapRepository scrapRepository;


    @Scheduled(initialDelay = 3000, fixedRate = 300000) // 5분마다 확인용
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
            /*TODO
            * 원래 사람인, 잡코리아 나눠서 각자 사이트 들어가서 값있으면 삭제 할려고 한건데 , 왜 안된건지 모르겠음
            * */
//                if (containsKeyword(scrap.getArticleUrl(), "saramin")) {
//                    System.out.println("url이 사람인인 경우 " + scrap.getId());
//
//                    String url = scrap.getArticleUrl();
//                    System.out.println("url : " + url);
//                    OkHttpClient client = new OkHttpClient.Builder()
//                            .connectionSpecs(List.of(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
//                            .build();
//                    Document doc = null;
//                    try {
//                        // OkHttp를 사용하여 HTTP/2.0 통신
//                        Request request = new Request.Builder()
//                                .url(url)
//                                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
//                                .header("Referer", "https://www.saramin.co.kr/zf_user/jobs/relay/view?isMypage=no&rec_idx=45868854&recommend_ids=eJxVj8ERAzEIA6vJHwwS4p1Crv8u4lzmbOe5s8KyEkKz6tLwV70TGhwZl%2FyHVYJhoUhjznDfyHS6PTbnrQPLqgKxkU12HU9BoYUot7bd294YO4ygHdYsv994bFVIf9i9w6Nn2WF99B44d8e518Q8ijylu%2BgDiSFAdg%3D%3D&view_type=avatar&gz=1&t_ref_scnid=805&t_ref_content=SRI_100_MAIN_AI_RCT_LOGOUT&t_ref=main&t_category=relay_view&t_content=relay_view_avatar#seq=0")
//                                .build();
//                        Response response = client.newCall(request).execute();
//                        String html = response.body().string();
//                        System.out.println("트라이를 들어오긴하잖아?");
//                        // Jsoup을 사용하여 HTML 파싱
//                        doc = Jsoup.parse(html);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//                    Elements elements =doc.select("#content > div.wrap_jview >section.jview jview-0-46007680");
//                    System.out.println("elements"+ elements);
//                    int count = elements.size(); // div.item_recruit의 개수
//                    System.out.println("count elements"+ count);
//
//
//                }
////                잡코리아 일때
//                else if (containsKeyword(scrap.getArticleUrl(), "jobkorea")) {
//                    System.out.println(" 잡코리아인 경우");
//                    String url = scrap.getArticleUrl();
//                    System.out.println("잡코리아 아이디 " + scrap.getId());
//                    OkHttpClient client = new OkHttpClient.Builder()
//                            .connectionSpecs(List.of(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
//                            .build();
//                    Document doc = null;
//                    try {
//                        Request request = new Request.Builder()
//                                .url(url)
//                                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
//                                .header("Referer", "https://www.jobkorea.co.kr/Recruit/GI_Read/42052468?Oem_Code=C1&logpath=1&stext=%EA%B0%9C%EB%B0%9C%EC%9E%90&listno=1")
//                                .build();
//                        Response response = client.newCall(request).execute();
//                        String html = response.body().string();
//                        doc = Jsoup.parse(html);
//                    } catch (Exception e) {
//                        System.out.println("잡코리아 삭제 연결 실패 ");
//                        e.printStackTrace();
//                    }
//
//                    Elements deadlineElements = doc.select("#container > section > div.deadline-view-progress");
//                    System.out.println("잡코리아 데드라인 태그 유무 : " + deadlineElements.size());
//                    if (deadlineElements.size() != 0) {
//                        scrapRepository.delete(scrap);
//                        System.out.println("잡코리아 Scrap 삭제됨 해당 ID :" + scrap.getId());
////
//                    }
//
//                }


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

    private boolean containsKeyword(String articleUrl, String keyword) {
        return articleUrl.contains(keyword);
    }

}
