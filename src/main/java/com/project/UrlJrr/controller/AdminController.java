package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Email;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.service.EmailService;
import com.project.UrlJrr.service.UserService;
import com.project.UrlJrr.utils.CronUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final EmailService emailService;


    @GetMapping("/page")
    public String adminPage(Model model) {
        List<User> users = userService.showListUser();
        String schedule = emailService.getEmailSchedule();
        String readableSchedule = CronUtils.convertToReadableFormat(schedule);
        model.addAttribute("myname", userService.getUsername());
        System.out.println("schedule이 크론표현식이 아닌가?" + schedule);
        model.addAttribute("schedule", readableSchedule);
        model.addAttribute("users", users);
        return "pages/user/adminPage";
    }

    @GetMapping("/userManagement")
    public String userManagement(Model model) {
        List<User> users = userService.showListUser();
        model.addAttribute("users", users);
        model.addAttribute("myname", userService.getUsername());
        return "pages/user/userManagement";

    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/userManagement";
    }

    @PostMapping("/updateEmailSchedule")
    public String updateEmailSchedule(@RequestParam("period") String period,
                                      @RequestParam("hour") int hour,
                                      @RequestParam("minute") int minute) {
        //초기화
        String newSchedule = "";
        newSchedule = CronUtils.convertToCronFormat(period + " " + hour + "시 " + minute + "분");

        // EmailService의 스케줄링 정보 업데이트 메서드 호출
        emailService.updateEmailSchedule(newSchedule);
        return "redirect:/admin/page";

    }


    @GetMapping("/emailLogPage")
    public String emailLogPage (Model model){
        List<Email> emailList =emailService.emailList();
        model.addAttribute("emails",emailList);
        return "pages/user/adminEmailLogPage";
    }


}
