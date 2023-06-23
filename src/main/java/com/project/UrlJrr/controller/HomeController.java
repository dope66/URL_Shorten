package com.project.UrlJrr.controller;

import com.project.UrlJrr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class HomeController {
    private final UserService userService;
    @GetMapping("/")
    public String home(Model model){
        String username = userService.getUsername();
        model.addAttribute("username",username);
        return "pages/index";
    }



}
