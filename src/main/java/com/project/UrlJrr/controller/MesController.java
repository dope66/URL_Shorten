package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.service.ProductLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mes")
public class MesController {
    private final ProductLogService productLogService;
    @GetMapping("/home")
    public String home(){
        return "pages/mes/home";
    }
    @GetMapping("/productLog")
    public String MesHome(){
        return "pages/mes/productLog";
    }
    @GetMapping("/worker")
    public String mesWorker(){
        return "pages/mes/workerRegister";
    }
    @GetMapping("/popUp")
    public String popUp(){
        return "pages/mes/popUp";
    }

    @GetMapping("/productLog/{id}")
    public String productLogDetail(@PathVariable Long id, Model model) {
        // id를 이용하여 해당 제품 로그 정보를 조회하고 모델에 담아서 전달
        ProductLog productLogDto = productLogService.getProductLogById(id);
        model.addAttribute("productLog", productLogDto);
        return "pages/mes/productModify";
    }


}
