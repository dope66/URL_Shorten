package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.service.EmailService;
import com.project.UrlJrr.service.ScrapingService;
import com.project.UrlJrr.service.UserService;
import com.project.UrlJrr.utils.CronUtils;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class ScrapingController {
    private final ScrapingService scrapingService;
    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/crawling/list")
    public String crawling(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            Model model) {

        Pageable pageable;
        Sort sort = Sort.by(sortField);

        if (sortOrder.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        List<Scrap> allScraps = scrapingService.scrapList();
        int scrapTableSize = allScraps.size();
        long maxId = scrapingService.findMaxId(allScraps);
        int unsentCount = scrapingService.calculateUnsentCount(allScraps);
        int deletedCount = scrapingService.calculateDeletedCount(maxId, scrapTableSize);

        Page<Scrap> scrapPage;
        if (StringUtils.hasText(search)) {
            pageable = PageRequest.of(page - 1, 10, sort);
            scrapPage = scrapingService.scrapSearchList(search, pageable);
        } else {
            pageable = PageRequest.of(page - 1, 10, sort);
            scrapPage = scrapingService.showListScrap(pageable);
        }

        int totalPages = scrapPage.getTotalPages();
        int groupSize = 10;
        int groupStart = (page - 1) / groupSize * groupSize + 1;
        int groupEnd = Math.min(groupStart + groupSize - 1, totalPages);

        model.addAttribute("scraps", scrapPage.getContent());
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("groupStart", groupStart);
        model.addAttribute("groupEnd", groupEnd);
        model.addAttribute("maxId", maxId);
        model.addAttribute("deleteCount", deletedCount);
        model.addAttribute("unsentCount", unsentCount);
        model.addAttribute("allScraps", allScraps);

        return "pages/matching/crawl";
    }


    @GetMapping("/crawling/apply")
    public String crawlingApply(Model model) {
        String username = userService.getUsername();
        User user = userService.getUserByUsername(username);
        String schedule = emailService.getEmailSchedule();
        String readableSchedule = CronUtils.convertToReadableFormat(schedule);
        model.addAttribute("user",user);
        model.addAttribute("schedule", readableSchedule);
        return "pages/matching/apply";
    }

    @GetMapping("/crawling/detail")
    public String crawlingDetail() {
        return "pages/matching/detail";
    }
    @PostMapping("/crawling/subScribe")
    public String subScribe(){
        userService.subScribeChange();
        return "redirect:/crawling/apply";
    }


    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> {
            p.setOneIndexedParameters(true);    // 1 페이지 부터 시작
            p.setMaxPageSize(10);       // 한 페이지에 10개씩 출력
        };
    }


}
