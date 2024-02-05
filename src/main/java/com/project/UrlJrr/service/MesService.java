package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.repository.MesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MesService {
    private final MesRepository mesRepository;

    public Page<ProductLog> findAll(Pageable pageable) {
        return mesRepository.findAll(pageable);

    }
    public List<ProductLog> productList(){
        List<ProductLog> productLogList = new ArrayList<>();
        return productLogList;
    };
    public Page<ProductLog> findByWorkerNameContaining(String workerName, Pageable pageable) {
        return mesRepository.findByWorkerNameContaining(workerName, pageable);
    }
    public long getTotalLogCount(){
        return mesRepository.count();
    }
}
