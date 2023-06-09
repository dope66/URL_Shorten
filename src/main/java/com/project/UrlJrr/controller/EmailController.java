package com.project.UrlJrr.controller;

import com.project.UrlJrr.email.Email;
import com.project.UrlJrr.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class EmailController {
    @Autowired
    private final EmailService emailService;

    @PostMapping("/send_email")
    public String sendEmail(Email email){
        String to = email.getTo();
        String subject = email.getSubject();
        String message = email.getMessage();
        emailService.sendEmail(to,subject,message);

        return "redirect:/";

    }
    @GetMapping("/email")
    public String emailForm(Model model,Email email) {
        model.addAttribute("email", email);
        return "message";
    }


}
