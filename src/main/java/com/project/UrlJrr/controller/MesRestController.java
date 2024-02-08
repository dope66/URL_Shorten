package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.ProductLogDto;
import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.service.MesService;
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
public class MesRestController {

    private final MesService mesService;

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
            // 시작과 끝이 두개다 null이  아닐때 ;
            if (StringUtils.hasText(search)) {
                // 검색 단어가 있을때
                productLogs = mesService.findByWorkDateBetweenAndWorkerNameContaining(startDate, endDate, search, pageable);
            } else {
                productLogs = mesService.findByWorkDateBetween(startDate, endDate, pageable);
            }
        } else if (StringUtils.hasText(search)) {
            // 날자가 없을때
            productLogs = mesService.findByWorkerNameContaining(search, pageable);
        } else {
            // 기본 리스트 열기
            productLogs = mesService.findAll(pageable);
        }
        PagedModel<EntityModel<ProductLog>> model = assembler.toModel(productLogs);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/totalProductLogCount")
    public ResponseEntity<Long> getTotalProductLogCount() {
        long totalProductLogCount = mesService.getTotalLogCount();
        return ResponseEntity.ok(totalProductLogCount);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registryProductLog(@RequestBody ProductLogDto productLogDto) {
        ProductLog newProductLog = mesService.register(productLogDto);
        return new ResponseEntity<>(newProductLog, HttpStatus.CREATED);
    }


    @GetMapping("/getProductionNumber")
    public ResponseEntity<?> getProductionNumbers(@RequestParam(name = "productionType") String productionType) {
        List<String> productionNumbers = mesService.getProductionNumbersByProductionType(productionType);
        return new ResponseEntity<>(productionNumbers, HttpStatus.OK);
    }

    @GetMapping("/getProductionName")
    public ResponseEntity<?> getProductionNames(@RequestParam(name = "productionType") String productionType) {
        List<String> productionNames = mesService.getProductionNamesByProductionType(productionType);
        return new ResponseEntity<>(productionNames, HttpStatus.OK);
    }


    @PutMapping("/modify/{id}")
    public ResponseEntity<?> modifyProductLog(@PathVariable("id") Long id, @RequestBody ProductLogDto productLogDto) {
        ProductLog existingProductLog = mesService.getProductLogById(id);
        if (existingProductLog == null) {
            return ResponseEntity.notFound().build();
        }
        existingProductLog.setWorkDate(productLogDto.getWorkDate());
        existingProductLog.setProductionType(productLogDto.getProductionType());
        existingProductLog.setProductionNumber(productLogDto.getProductionNumber());
        existingProductLog.setProductionName(productLogDto.getProductionName());
        existingProductLog.setProduction(productLogDto.getProduction());
        existingProductLog.setWorkerName(productLogDto.getWorkerName());

        ProductLog updatedProductLog = mesService.modifyProductLog(existingProductLog);
        return ResponseEntity.ok(updatedProductLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductLog(@PathVariable("id") Long id) {
        mesService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

