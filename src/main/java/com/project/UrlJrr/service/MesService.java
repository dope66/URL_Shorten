package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.ProductLogDto;
import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.repository.MesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public ProductLog register(ProductLogDto productLogDto){
        return mesRepository.save(productLogDto.toEntity());
    }
    public ProductLog modifyProductLog(ProductLog productLog) {
        return mesRepository.save(productLog);
    }

    public List<String> getProductionNumbersByProductionType(String productionType) {
        return mesRepository.findProductNumbersByProductionType(productionType);
    }
    public List<String> getProductionNamesByProductionType(String productionType){
        return mesRepository.findProductNameByProductionType(productionType);
    }
    public void deleteProduct(Long productId){mesRepository.deleteById(productId);};

    public ProductLog getProductLogById(Long id) {
        Optional<ProductLog> optionalProductLog = mesRepository.findById(id);
        return optionalProductLog.orElse(null);
    }

}
