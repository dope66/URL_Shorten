package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.service.ScrapingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ScrapingController {
    @Autowired
    private final ScrapingService scrapingService;

    @GetMapping("/crawl")
    public String crawl(Model model) throws IOException {
            List<Scrap> scraps = scrapingService.ScrapSaram();
            model.addAttribute("scraps", scraps);

        return "crawl";
    }

}
