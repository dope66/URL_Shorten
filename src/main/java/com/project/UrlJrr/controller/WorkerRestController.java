package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.ProcessWorkerDto;
import com.project.UrlJrr.entity.ProcessWorker;
import com.project.UrlJrr.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/worker")
public class WorkerRestController {
    private final WorkerService workerService;

    @PostMapping("/register")
    public ResponseEntity<?> registryWorker(@RequestBody ProcessWorkerDto processWorkerDto){
        ProcessWorker newWorker = workerService.workerRegister(processWorkerDto);
        return new ResponseEntity<>(newWorker, HttpStatus.CREATED);
    }

}
