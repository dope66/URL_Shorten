package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.ProductLogDto;
import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.service.ProductLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mes")
public class ProductLogRestController {

    private final ProductLogService productLogService;

    @GetMapping("/productLogList")
    public ResponseEntity<?> getTestList() {
        List<ProductLog> ProductLogs;
        ProductLogs = productLogService.findAll();
        return new ResponseEntity<>(ProductLogs, HttpStatus.OK);
    }

    @GetMapping("/totalProductLogCount")
    public ResponseEntity<Long> getTotalProductLogCount() {
        long totalProductLogCount = productLogService.getTotalLogCount();
        return ResponseEntity.ok(totalProductLogCount);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registryProductLog(@RequestBody ProductLogDto productLogDto) {
        ProductLog newProductLog = productLogService.register(productLogDto);
        return new ResponseEntity<>(newProductLog, HttpStatus.CREATED);
    }

    @GetMapping("/getProductionType")
    public ResponseEntity<?> getProductionTypes() {
        List<String> productionTypes = productLogService.getProductionTypes();
        return new ResponseEntity<>(productionTypes, HttpStatus.OK);
    }

    @GetMapping("/getProductionNumber")
    public ResponseEntity<?> getProductionNumbers(@RequestParam(name = "productionType") String productionType) {
        List<String> productionNumbers = productLogService.getProductionNumbersByProductionType(productionType);
        return new ResponseEntity<>(productionNumbers, HttpStatus.OK);
    }

    @GetMapping("/getWorkerName")
    public ResponseEntity<List<String>> getWorkerName() {
        List<String> workerNames = productLogService.getWorkerNames();
        return new ResponseEntity<>(workerNames, HttpStatus.OK);
    }

    @GetMapping("/getProductionName")
    public ResponseEntity<?> getProductionNames(@RequestParam(name = "productionNumber") String productionNumber) {
        List<String> productionNames = productLogService.getProductionNamesByProductionNumber(productionNumber);
        return new ResponseEntity<>(productionNames, HttpStatus.OK);
    }


    @PutMapping("/modify/{id}")
    public ResponseEntity<?> modifyProductLog(@PathVariable("id") Long id, @RequestBody ProductLogDto productLogDto) {
        ProductLog existingProductLog = productLogService.getProductLogById(id);
        if (existingProductLog == null) {
            return ResponseEntity.notFound().build();
        }
        existingProductLog.setWorkDate(productLogDto.getWorkDate());
        existingProductLog.setProductionType(productLogDto.getProductionType());
        existingProductLog.setProductionNumber(productLogDto.getProductionNumber());
        existingProductLog.setProductionName(productLogDto.getProductionName());
        existingProductLog.setProduction(productLogDto.getProduction());
        existingProductLog.setWorkerName(productLogDto.getWorkerName());

        ProductLog updatedProductLog = productLogService.modifyProductLog(existingProductLog);
        return ResponseEntity.ok(updatedProductLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductLog(@PathVariable("id") Long id) {
        productLogService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
