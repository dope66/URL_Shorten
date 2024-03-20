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
    @GetMapping("/AllListToday")
    public ResponseEntity<List<ProductionOrder>> getAllListToday(){
        List<ProductionOrder> productionOrderListToday = productionOrderService.findAllProducedToday();
        return ResponseEntity.ok(productionOrderListToday);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ProductOrderDto productOrderDto){
        System.out.println("해당하는 저장 메서드에 진입");
        ProductionOrder newProductionOrder = productionOrderService.register(productOrderDto);
        System.out.println("저장");
        return new ResponseEntity<>(newProductionOrder, HttpStatus.CREATED);
    }


}
