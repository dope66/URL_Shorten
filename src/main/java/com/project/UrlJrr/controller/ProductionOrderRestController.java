package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.ProductionOrder;
import com.project.UrlJrr.service.ProductionOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class ProductionOrderRestController {
    private final ProductionOrderService productionOrderService;


    @GetMapping("/list")
    public ResponseEntity<List<ProductionOrder>> getProductionOrderList(@RequestParam String processName, @RequestParam String equipmentName) {
        List<ProductionOrder> productionOrders = productionOrderService.findOrdersByProcessAndEquipment(processName, equipmentName);
        return ResponseEntity.ok(productionOrders);
    }

}
