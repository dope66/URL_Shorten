package com.project.UrlJrr.service;


import com.project.UrlJrr.domain.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrapingService {
    private final ScrapRepository scrapRepository;

    public List<Scrap> ScrapSaram() throws IOException {
        List<Scrap> scraps = new ArrayList<>();
        // 기본 url 설정
        String url = "https://www.saramin.co.kr/zf_user/search/recruit?search_area=main&search_done=y&search_optional_item=n&searchType=recently&searchword=%EA%B0%9C%EB%B0%9C%EC%9E%90&recruitPage=1&recruitSort=relation&recruitPageCount=30&inner_com_type=&company_cd=0%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C9%2C10&show_applied=&quick_apply=&except_read=&ai_head_hunting=";
        String articleUrlPrefix = "https://www.saramin.co.kr";
        // Jsoup을 이용한 연결 설정
        Connection connection = Jsoup.connect(url)
                .header("User-Agent", "Mozilla/5.0")
                .timeout(10000);

        Document doc = connection.get();
        Elements elements = doc.select("#recruit_info_list > div.content > div.item_recruit");
        int count = elements.size(); // div.item_recruit의 개수
        System.out.println("div.item_recruit 개수: " + count);

        for (Element element : elements) {
            String articleText = element.select("div.area_job > h2.job_tit").text();
            String articleUrl = element.select("div.area_job > h2.job_tit > a").attr("href");
            String company = element.select("div.area_corp > strong.corp_name > a").text();
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
            Scrap scrap = new Scrap(null, articleText, articleUrl, company);
            scraps.add(scrap);
            // 제외
            if (!isDuplicate) {
                scrapRepository.save(scrap);
            }
        }

        return scraps;
    }


}

