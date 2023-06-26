package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.repository.ScrapRepository;
import com.project.UrlJrr.service.ScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/crawling/list")
    public String crawling(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            Model model) {

        Page<Scrap> scrapPage;
        Pageable pageable;
        Sort sort = Sort.by(sortField);

        if (sortOrder.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        if (StringUtils.hasText(search)) {
            pageable = PageRequest.of(page - 1, 10, sort);
            scrapPage = scrapingService.ScrapSearchList(search, pageable);
            model.addAttribute("scraps", scrapPage.getContent());
        } else {
            pageable = PageRequest.of(page - 1, 10, sort);
            scrapPage = scrapingService.showListScrap(pageable);
            model.addAttribute("scraps", scrapPage.getContent());

        }
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", scrapPage.getTotalPages());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);

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
