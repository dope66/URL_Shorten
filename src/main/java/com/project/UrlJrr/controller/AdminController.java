package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.UserDto;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/page")
    public String adminPage(){
        return "pages/user/adminPage";
    }
    @GetMapping("/userManagement")
    public String userManagement(Model model){
        List<User> users = userService.showListUser();
        model.addAttribute("users",users);
        return "pages/user/userManagement";

    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId){
        userService.deleteUser(userId);
        return "redirect:/admin/userManagement";
    }

}
