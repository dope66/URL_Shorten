package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.ProductLogDto;
import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.service.ProductLogService;
import com.project.UrlJrr.service.ProductionTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mes")
public class ProductLogRestController {

    private final ProductLogService productLogService;
    private final ProductionTestService productionTestService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> productLogList(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(name = "search", required = false) String search,
            @PageableDefault(sort = "workDate", direction = Sort.Direction.DESC) Pageable pageable,
            PagedResourcesAssembler<ProductLog> assembler
    ) {
        Page<ProductLog> productLogs;
        if (startDate != null && endDate != null) {
            if (StringUtils.hasText(search)) {
                // 검색 단어가 있을때
                productLogs = productLogService.findByWorkDateBetweenAndWorkerNameContaining(startDate, endDate, search, pageable);
            } else {
                productLogs = productLogService.findByWorkDateBetween(startDate, endDate, pageable);
            }
        } else if (StringUtils.hasText(search)) {
            // 날자가 없을때
            productLogs = productLogService.findByWorkerNameContaining(search, pageable);
        } else {
            // 기본 리스트 열기
            productLogs = productLogService.findAll(pageable);
        }
        PagedModel<EntityModel<ProductLog>> model = assembler.toModel(productLogs);
        return new ResponseEntity<>(model, HttpStatus.OK);
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
