package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @GetMapping("/register")
    public String register() {
        return "pages/user/register";
    }

    @GetMapping("/login")
    public String login() {
        return "pages/user/login";
    }

    @GetMapping("/modify")
    public String userPage(@AuthenticationPrincipal Model model) {
        String username = userService.getUsername();
        User user = userService.getUserByUsername(username);
        String userSkillStack = user.getSkillStack();
        model.addAttribute("user", user);
        model.addAttribute("userSkillStack", userSkillStack);

        return "pages/user/userPage";
    }

    @GetMapping("/changePassword")
    public String changePassword() {
        return "pages/user/changePassword";
    }


    @GetMapping("/findPassword")
    public String findPassword() {
        return "pages/user/findPassword";
    }

    @GetMapping("/userSkillStack")
    @ResponseBody
    public Map<String, Object> getUserSkillStack() {
        Map<String, Object> response = new HashMap<>();
        String username = userService.getUsername();
        User user = userService.getUserByUsername(username);
        String userSkillStack = user.getSkillStack();
        response.put("userSkillStack", userSkillStack);
        return response;
    }

    @PostMapping("/findPassword")
    public String findPasswordProc(@RequestParam("email") String email, @RequestParam("username") String username, Model model) {
        String result = userService.userEamilCheck(email, username);
        if (result.isEmpty()) {
            userService.resetPasswordAndSendEmail(email, username);
            model.addAttribute("successMessage", "이메일을 성공적으로 보냈습니다! 이메일을 확인해주세요.");
        } else {
            model.addAttribute("errorMessage", result);

        }
        return "pages/user/login";

    }



}
