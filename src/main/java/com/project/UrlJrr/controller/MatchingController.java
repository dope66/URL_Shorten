package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.repository.ScrapRepository;
import com.project.UrlJrr.service.AuthService;
import com.project.UrlJrr.service.MatchingService;
import com.project.UrlJrr.service.ScrapingService;
import com.project.UrlJrr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {
    private final UserService userService;
    private final ScrapingService scrapingService;
    private final AuthService authService;
    private final MatchingService matchingService;
    private final ScrapRepository scrapRepository;
    @GetMapping("/{scrapId}")
    public String matchingPage(@PathVariable("scrapId") Long scrapId, Model model){
        String username = userService.getUsername();
        User user = userService.getUserByUsername(username);
        Optional<Scrap> scrapOptional = scrapRepository.findById(scrapId);
        if(scrapOptional.isPresent()){
            Scrap scrap = scrapOptional.orElseThrow(() -> new IllegalArgumentException("Invalid Scrap ID: " + scrapId));
            int matchingScore = matchingService.calculateMatchingScore(user, scrapId);
            model.addAttribute("articleUrl", scrap.getArticleUrl());
            model.addAttribute("user",user);
            model.addAttribute("matchingScore", matchingScore);
            model.addAttribute("scrap",scrap);
            return "pages/matching/matchingDetail";
        }else{
            model.addAttribute("errorMessage");
            return "pages/matching/deleteScrap";
        }




    }
}
