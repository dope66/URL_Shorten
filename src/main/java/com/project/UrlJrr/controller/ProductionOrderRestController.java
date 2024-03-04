package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.ProductOrderDto;
import com.project.UrlJrr.entity.ProductionOrder;
import com.project.UrlJrr.service.ProductionOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/AllList")
    public ResponseEntity<List<ProductionOrder>> getAllList(){
        List<ProductionOrder> productionOrderList= productionOrderService.findAll();
        return ResponseEntity.ok(productionOrderList);
    }

    @PostMapping("/register")
    public ResponseEntity<?> regist(@RequestBody ProductOrderDto productOrderDto){
        ProductionOrder newProductionOrder = productionOrderService.register(productOrderDto);
        return new ResponseEntity<>(newProductionOrder, HttpStatus.CREATED);
    }


}
