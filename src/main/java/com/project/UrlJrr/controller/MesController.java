package com.project.UrlJrr.controller;

import com.project.UrlJrr.service.MesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mes")
public class MesController {
    private final MesService mesService;
    @GetMapping("/productLog")
    public String MesHomeContorller(){
        return "/pages/mes/productLog";
    }


}
