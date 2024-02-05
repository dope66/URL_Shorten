package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.service.MesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mes")
public class MesRestController {

    private final MesService mesService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> productLogList(
            @RequestParam(name = "search", required = false) String search,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            PagedResourcesAssembler<ProductLog> assembler
    ) {
        Page<ProductLog> productLogs;
        if (StringUtils.hasText(search)) {
            productLogs = mesService.findByWorkerNameContaining(search, pageable);
        } else {
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
}
