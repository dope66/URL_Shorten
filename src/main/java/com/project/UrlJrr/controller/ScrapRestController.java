package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Scrap;
import com.project.UrlJrr.service.ScrapingService;
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
@RequestMapping("/api/scrap")
public class ScrapRestController {
    private final ScrapingService scrapingService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> scrapList(
            @RequestParam(name = "search", required = false) String search,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            PagedResourcesAssembler<Scrap> assembler) {

        Page<Scrap> scraps;

        if (StringUtils.hasText(search)) {
            scraps = scrapingService.findByTitleContaining(search, pageable);
        } else {
            scraps = scrapingService.findAll(pageable);
        }

        PagedModel<EntityModel<Scrap>> model = assembler.toModel(scraps);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/totalScrapCount")
    public ResponseEntity<Long> getTotalScrapCount() {
        long totalScrapCount = scrapingService.getTotalScrapCount();
        return ResponseEntity.ok(totalScrapCount);
    }
}
