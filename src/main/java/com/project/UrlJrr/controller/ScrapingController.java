package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import com.project.UrlJrr.service.ScrapingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class ScrapingController {
    private final ScrapingService scrapingService;
    private final ScrapRepository scrapRepository;

    @GetMapping("/crawling/list")
    public String crawling(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) String search, Model model) {

        Page<Scrap> scrapPage;
        Pageable pageable;

        if (StringUtils.hasText(search)) {
            pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
            scrapPage = scrapingService.ScrapSearchList(search, pageable);
        } else {
            pageable = PageRequest.of(page - 1, 10, Sort.by("id").descending());
            scrapPage = scrapingService.showListScrap(pageable);

        }
        model.addAttribute("scraps", scrapPage.getContent());
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", scrapPage.getTotalPages());

        return "pages/matching/crawl";
    }


    @GetMapping("/crawling/apply")
    public String crawlingApply() {
        return "pages/matching/apply";
    }

    @GetMapping("/crawling/detail")
    public String crawlingDetail() {
        return "pages/matching/detail";
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> {
            p.setOneIndexedParameters(true);    // 1 페이지 부터 시작
            p.setMaxPageSize(10);       // 한 페이지에 10개씩 출력
        };
    }
}
