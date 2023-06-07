package com.project.UrlJrr.controller;

import com.project.UrlJrr.domain.UrlMapping;
import com.project.UrlJrr.domain.UrlMappingDto;
import com.project.UrlJrr.repository.UrlMappingRepository;
import com.project.UrlJrr.service.UrlMappingService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UrlMappingController {
    @Autowired
    private final UrlMappingService urlMappingService;


    @GetMapping("/")
    public String home(UrlMappingDto urlMappingDto) {
        return "index";
    }

    @PostMapping("/urlMapping")
    public String convertUrl(@ModelAttribute("urlMappingDto") UrlMappingDto urlMappingDto, Model model) {
        String originalUrl = urlMappingDto.getOriginalUrl();
        String shortUrl = urlMappingService.generateShortUrl(originalUrl);
        // 생성된 단축 URL을 모델에 추가
        model.addAttribute("shortUrl", shortUrl);
        return "resultPage";
    }
    @GetMapping("/{shortUrl}")
    public String redirectOriginalUrl(@PathVariable String shortUrl){
        String originalUrl = urlMappingService.getOriginalUrl(shortUrl);
        if(originalUrl!=null){
            return "redirect:"+originalUrl;
        }else{
            // 유효하지 않은 단축 url
            return "invalidUrlPage";
        }

    }

}
