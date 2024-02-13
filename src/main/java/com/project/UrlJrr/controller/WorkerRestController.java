package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.ProcessWorkerDto;
import com.project.UrlJrr.entity.ProcessWorker;
import com.project.UrlJrr.service.WorkerService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/worker")
public class WorkerRestController {
    private final WorkerService workerService;

    /*
     * 공정원 가입
     * */
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registryWorker(@RequestPart(name = "image", required = false) MultipartFile imageFile,
                                            @ModelAttribute ProcessWorkerDto processWorkerDto) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = "image_" + System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String filePath = "/static/assets/images/" + fileName; // 이미지를 저장할 디렉토리 경로
                System.out.println("filePath : " + filePath);
                imageFile.transferTo(new File(filePath));
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Failed to save image", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        ProcessWorker newWorker = workerService.workerRegister(processWorkerDto);
        return new ResponseEntity<>(newWorker, HttpStatus.CREATED);
    }

    @GetMapping(value="/list" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> workerList(@RequestParam(name = "search", required = false) String search,
                                        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                        PagedResourcesAssembler<ProcessWorker> assembler) {

        Page<ProcessWorker> ProcessWorkers;

        if (StringUtils.hasText(search)) {
            ProcessWorkers = workerService.findByWorkerNameContaining(search, pageable);
        } else {
            ProcessWorkers = workerService.findAll(pageable);
        }
        PagedModel<EntityModel<ProcessWorker>> model = assembler.toModel(ProcessWorkers);
        return new ResponseEntity<>(model, HttpStatus.OK);

    }
    @GetMapping("/totalEmployeeCount")
    public ResponseEntity<Long> getTotalEmployeeCount(){
        long totalEmployeeCount = workerService.getTotalEmployeeCount();
        return ResponseEntity.ok(totalEmployeeCount);
    }


}
