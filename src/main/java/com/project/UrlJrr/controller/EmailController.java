package com.project.UrlJrr.controller;

import com.project.UrlJrr.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class EmailController {
    @Autowired
    private final EmailService emailService;


}
