package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.UserDto;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.service.EmailService;
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
    private final EmailService emailService;

    @GetMapping("/page")
    public String adminPage() {
        return "pages/user/adminPage";
    }

    @GetMapping("/userManagement")
    public String userManagement(Model model) {
        List<User> users = userService.showListUser();
        model.addAttribute("users", users);
        return "pages/user/userManagement";

    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/userManagement";
    }

    @PostMapping("/updateEmailSchedule")
    public String updateEmailSchedule(@RequestParam("period") String period,
                                      @RequestParam("hour") int hour) {
        //초기화
        String newSchedule = "";
        if (period.equals("am")) {
            newSchedule = "0 0 " + hour + " * * ?";
        } else if (period.equals("pm")) {
            newSchedule = "0 0 " + (hour + 12) + " * * ?";
        }
        // EmailService의 스케줄링 정보 업데이트 메서드 호출
        emailService.updateEmailSchedule(newSchedule);
        return "pages/user/adminPage";

    }


}
