package com.project.UrlJrr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/page")
public class PageRestController {

    @GetMapping("/email")
    public String emailLogPage() {
        return "pages/user/adminEmailLogPage";
    }


}
