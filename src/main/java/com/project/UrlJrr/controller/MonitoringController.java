package com.project.UrlJrr.controller;

import com.project.UrlJrr.dto.MonitoringDto;
import com.project.UrlJrr.entity.Monitoring;
import com.project.UrlJrr.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/monitor")
public class MonitoringController {
    private final MonitoringService monitoringService;
    @PostMapping("/Test")
    public ResponseEntity<?> MonitoringTest(@RequestBody MonitoringDto monitoringDto){
        Monitoring newMonitoring = monitoringService.register(monitoringDto);
        return new ResponseEntity<>(newMonitoring, HttpStatus.CREATED);
    }

    @GetMapping("/List")
    public ResponseEntity<?> getMoniotringList(){
        List<Monitoring> Monitorings;
        Monitorings = monitoringService.findAll();
        return  new ResponseEntity<>(Monitorings,HttpStatus.OK);
    }



}
