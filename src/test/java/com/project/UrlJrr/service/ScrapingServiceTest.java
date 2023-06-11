package com.project.UrlJrr.service;

import com.project.UrlJrr.repository.ScrapRepository;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

class ScrapingServiceTest {

    @Mock
    private ScrapRepository scrapRepository;

    @InjectMocks
    private ScrapingService scrapingService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(scrapingService).build();
    }

    @Test


// ...

    void testScrapSaram() throws Exception {
        // 기본 url 설정
        String url = "https://www.saramin.co.kr/zf_user/search?searchType=search&searchword=%EA%B0%9C%EB%B0%9C&recruitPage=1&recruitSort=relation&recruitPageCount=100&inner_com_type=&company_cd=0%2C1%2C2%2C3%2C4%2C5%2C6%2C7&show_applied=&quick_apply=&except_read=&ai_head_hunting=&mainSearch=y";
        String articleUrlPrefix = "https://www.saramin.co.kr";
        // Jsoup을 이용한 연결 설정
        Connection connection = Jsoup.connect(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
                .header("Referer", "https://www.saramin.co.kr/zf_user/jobs/relay/view?isMypage=no&rec_idx=45868854&recommend_ids=eJxVj8ERAzEIA6vJHwwS4p1Crv8u4lzmbOe5s8KyEkKz6tLwV70TGhwZl%2FyHVYJhoUhjznDfyHS6PTbnrQPLqgKxkU12HU9BoYUot7bd294YO4ygHdYsv994bFVIf9i9w6Nn2WF99B44d8e518Q8ijylu%2BgDiSFAdg%3D%3D&view_type=avatar&gz=1&t_ref_scnid=805&t_ref_content=SRI_100_MAIN_AI_RCT_LOGOUT&t_ref=main&t_category=relay_view&t_content=relay_view_avatar#seq=0")
                .timeout(10000);

        Document doc = connection.get();


        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(List.of(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
                .build();

        try {
            // OkHttp를 사용하여 HTTP/2.0 통신
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String html = response.body().string();

            // Jsoup을 사용하여 HTML 파싱
            doc = Jsoup.parse(html);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // 파일에 저장할 경로와 파일명 설정
        String filePath = "output.html";

        // HTML 파일 작성을 위한 BufferedWriter 생성
        // HTML 파일 작성을 위한 BufferedWriter 생성
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8))) {
            // HTML 내용을 파일에 작성
            writer.write(doc.html());
        } catch (IOException e) {
            e.printStackTrace();
        }

        doc = Jsoup.parse(doc.html());
        Elements elements = doc.select("#recruit_info_list > div.content > div");

        int count = elements.size(); // div.item_recruit의 개수
        System.out.println("div.item_recruit 개수: " + count);
        for (Element element : elements) {
            String articleText = element.select("div.area_job > h2.job_tit > a > span").text();
            String articleUrl = element.select("div.area_job > h2.job_tit > a").attr("href");
            String company = element.select("div.area_corp > strong.corp_name > a").text();
            System.out.println(articleText + " / " + articleUrl + " / " + company);

        }
    }
}
