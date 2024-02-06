package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.repository.MesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MesService {
    private final MesRepository mesRepository;

    public Page<ProductLog> findAll(Pageable pageable) {
        return mesRepository.findAll(pageable);

    }

    public Page<ProductLog> findByWorkerNameContaining(String workerName, Pageable pageable) {
        return mesRepository.findByWorkerNameContaining(workerName, pageable);
    }

    public long getTotalLogCount() {
        return mesRepository.count();
    }

    public Page<ProductLog> findByWorkDateBetweenAndWorkerNameContaining(Date startDate, Date endDate, String workerName, Pageable pageable) {
        return mesRepository.findByWorkDateBetweenAndWorkerNameContaining(startDate, endDate, workerName, pageable);
    }

    public Page<ProductLog> findByWorkDateBetween(Date startDate, Date endDate, Pageable pageable) {
        return mesRepository.findByWorkDateBetween(startDate, endDate, pageable);
    }

}
