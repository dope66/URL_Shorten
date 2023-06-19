package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.UserDto;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {
    private final UserService userService;
    @GetMapping("/")
    public String home(Model model){
        String username = userService.getUsername();
        model.addAttribute("username",username);
        return "index";
    }



}
