package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.UserDto;
import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "register";
    }

    @PostMapping("/registerProc")
    public String registerProc(@Validated  UserDto userDto,Errors errors, Model model) {
        if(errors.hasErrors()){
            model.addAttribute("userDto",userDto);
            Map<String,String> validatorResult = userService.validatedHandling(errors);
            model.addAttribute("validatorResult", validatorResult);
            return "register";
        }
        try{
            User newUser = userService.register(userDto);
            System.out.println("유저 아이디 생성 성공");
            model.addAttribute("successMessage", "회원 가입이 성공적으로 완료되었습니다.");
            return "redirect:/";
        }catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userDto", userDto);
            model.addAttribute("showErrorMessage", true);
            return "register";
        }


    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }




}
