package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.UserDto;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
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
    public String register(Model model) {
//        model.addAttribute("userDto", new UserDto());
        return "pages/user/register";
    }

    @PostMapping("/registerProc")
    public String registerProc(@Validated UserDto userDto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("userDto", userDto);
            Map<String, String> validatorResult = userService.validatedHandling(errors);
            model.addAttribute("validatorResult", validatorResult);
            return "pages/user/register";
        }
        try {
            User newUser = userService.register(userDto);
            System.out.println("유저 아이디 생성 성공");
            model.addAttribute("successMessage", "회원 가입이 성공적으로 완료되었습니다.");
            return "redirect:/";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userDto", userDto);
            model.addAttribute("showErrorMessage", true);
            return "pages/user/register";
        }


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
        System.out.println(user.toString());
        return "pages/user/userPage";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId, HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(userId);
        // 로그아웃 처리를 위해 SecurityContextLogoutHandler를 사용하여 세션 무효화
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, null);
        // 회원 삭제 후에는 다시 로그인 페이지 등으로 리다이렉트하도록 합니다.
        return "redirect:/"; // 로그인 페이지 경로로 변경해주세요.
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model) {

        return "pages/user/changePassword";
    }


    @PostMapping("/changePassword")
    public String changePasswordProc(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        String resultMessage = userService.changePassword(currentPassword, newPassword, confirmPassword);

        if (resultMessage.startsWith("error:")) {
            model.addAttribute("error", resultMessage.substring(6));
            return "pages/user/changePassword";
        } else {
            model.addAttribute("success", resultMessage);
            model.addAttribute("redirect", true); // 변경 성공 시 리다이렉트 여부 추가
            return "pages/user/changePassword";
        }


    }

    @GetMapping("/findPassword")
    public String findPassword() {

        return "pages/user/findPassword";

    }

    @PostMapping("/findPassword")
    public String findPasswordProc(@RequestParam("email") String email, @RequestParam("username") String username, Model model) {
        String result = userService.userEamilCheck(email, username);
        if (result.isEmpty()) {
            userService.resetPasswordAndSendEmail(email, username);
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", result);
            return "pages/user/findPassword";
        }


    }

    @GetMapping("/userSkillStack")
    @ResponseBody
    public Map<String, Object> getUserSkillStack(Model model) {
        Map<String, Object> response = new HashMap<>();
        String username = userService.getUsername();
        User user = userService.getUserByUsername(username);
        String userSkillStack = user.getSkillStack();
        response.put("userSkillStack", userSkillStack);
        return response;
    }


}
