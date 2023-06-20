package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.UrlMappingDto;
import com.project.UrlJrr.service.UrlMappingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/Short")
public class UrlMappingController {
    @Autowired
    private final UrlMappingService urlMappingService;

    @GetMapping("")
    public String home(UrlMappingDto urlMappingDto) {
        return "urlShort";
    }

    @PostMapping("/urlMapping")
    public String convertUrl(@ModelAttribute("urlMappingDto") UrlMappingDto urlMappingDto, HttpServletRequest request,Model model) {
        String serverName =request.getServerName();
        model.addAttribute("serverName",serverName);
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
