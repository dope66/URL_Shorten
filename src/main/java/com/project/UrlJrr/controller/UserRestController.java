package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.UserDto;
import com.project.UrlJrr.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/user")
public class UserRestController {

    private final UserService userService;


    @PostMapping("/modify")

    public String modifySave(@Validated @RequestBody UserDto userDto, Errors errors, Model model) {
        if (errors.hasErrors())
            return "nk1";

        try {
            userService.update(userDto);
            return "ok";
        } catch (IllegalStateException e) {
            return "nk2";
        }
    }


}