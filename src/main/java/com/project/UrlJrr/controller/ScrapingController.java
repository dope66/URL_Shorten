package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.service.ScrapingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String crawling(Scrap scrap, @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           HttpServletRequest request, Model model,
                           @RequestParam(required = false) String search) {

        Page<Scrap> scrapPage;
        Pageable pageable = PageRequest.of(page - 1, size);
        if (StringUtils.hasText(search)) {
            scrapPage = scrapingService.ScrapSearchList(search, pageable);
        } else {
            scrapPage = scrapingService.showListScrap(pageable);
        }
        int totalPages = scrapPage.getTotalPages();
        int startPage = Math.max(1, page - ((page - 1) % 10));
        int endPage = Math.min(startPage + 9, totalPages);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("scraps", scrapPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", scrapPage.getTotalPages());
        model.addAttribute("search", search);

        return "pages/matching/crawl";
    }
        @GetMapping("/crawling/apply")
        public String crawlingApply(){
            return "pages/matching/apply";
        }

        @GetMapping("/crawling/detail")
        public String crawlingDetail(){
            return "pages/matching/detail";
        }

    }
