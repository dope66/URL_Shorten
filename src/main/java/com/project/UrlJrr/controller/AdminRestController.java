package com.project.UrlJrr.controller;

import com.project.UrlJrr.entity.Email;
import com.project.UrlJrr.service.EmailService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminRestController {
    private final EmailService emailService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> emailLogPage(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                          Pageable pageable, PagedResourcesAssembler<Email> assembler) {
        Page<Email> emails = emailService.findAll(pageable);
        PagedModel<EntityModel<Email>> model = assembler.toModel(emails);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }
    @GetMapping("/totalLog")
    public ResponseEntity<Long> getTotalEmailCount(){
        long totalLog = emailService.getTotalEmailLog();
        return ResponseEntity.ok(totalLog);
    }

}
