package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.ProcessWorkerDto;
import com.project.UrlJrr.entity.ProcessWorker;
import com.project.UrlJrr.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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


//    @GetMapping("/list")
//    public ResponseEntity<?>workerList(){
//        return new ResponseEntity<>(,HttpStatus.OK);
//    }

}
