package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.MonitoringDto;
import com.project.UrlJrr.entity.Monitoring;
import com.project.UrlJrr.repository.MonitoringRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MonitoringService {
    private final MonitoringRepository monitoringRepository;

    public Monitoring register(MonitoringDto monitoringDto) {
        return monitoringRepository.save(monitoringDto.toEntity());
    }

    public List<Monitoring> findAll() {
        return monitoringRepository.findAll();
    }
}
