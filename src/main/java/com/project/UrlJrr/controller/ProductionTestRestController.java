package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.ProductionTestDto;
import com.project.UrlJrr.entity.ProductionTest;
import com.project.UrlJrr.service.ProductionTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Test")
public class ProductionTestRestController {
    private final ProductionTestService productionTestService;
    @PostMapping("/productionTest")
    public ResponseEntity<?> productionTest(@RequestBody ProductionTestDto productionTestDto){
        ProductionTest newProductionTest = productionTestService.register(productionTestDto);
        return new ResponseEntity<>(newProductionTest, HttpStatus.CREATED);
    }

}
