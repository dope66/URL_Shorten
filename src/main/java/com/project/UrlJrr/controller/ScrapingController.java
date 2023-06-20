package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.service.ScrapingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;


@Controller
@RequiredArgsConstructor
public class ScrapingController {
    private final ScrapingService scrapingService;
    @GetMapping("/crawling/list")
    public String crawling(Scrap scrap, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                            HttpServletRequest request, Model model) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
        Page<Scrap> scrapPage = scrapingService.showListScrap(pageable);
        model.addAttribute("scraps", scrapPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", scrapPage.getTotalPages());
        return "crawl";
    }





}
