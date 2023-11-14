package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.service.EmailService;
import com.project.UrlJrr.service.ScrapingService;
import com.project.UrlJrr.service.UserService;
import com.project.UrlJrr.utils.CronUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class ScrapingController {
    private final ScrapingService scrapingService;
    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/crawling/list")
    public String crawling() {
        return "pages/matching/crawl";
    }

    // 구독정보 갱신 페이지열기
    @GetMapping("/crawling/apply")
    public String crawlingApply(Model model) {
        String username = userService.getUsername();
        User user = userService.getUserByUsername(username);
        String schedule = emailService.getEmailSchedule();
        String readableSchedule = CronUtils.convertToReadableFormat(schedule);
        model.addAttribute("user", user);
        model.addAttribute("schedule", readableSchedule);
        return "pages/matching/apply";
    }

    // 정보 주는거
    @PostMapping("/crawling/subScribe")
    public String subScribe() {
        userService.subScribeChange();
        return "redirect:/crawling/apply";
    }

}
