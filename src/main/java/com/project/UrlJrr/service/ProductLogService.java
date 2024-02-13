package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.ProductLogDto;
import com.project.UrlJrr.entity.ProductLog;
import com.project.UrlJrr.repository.ProductLogRepository;
import com.project.UrlJrr.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductLogService {
    private final ProductLogRepository productLogRepository;
    private final WorkerRepository workerRepository;

    public Page<ProductLog> findAll(Pageable pageable) {
        return productLogRepository.findAll(pageable);

    }

    public Page<ProductLog> findByWorkerNameContaining(String workerName, Pageable pageable) {
        return productLogRepository.findByWorkerNameContaining(workerName, pageable);
    }

    public long getTotalLogCount() {
        return productLogRepository.count();
    }

    public Page<ProductLog> findByWorkDateBetweenAndWorkerNameContaining(Date startDate, Date endDate, String workerName, Pageable pageable) {
        return productLogRepository.findByWorkDateBetweenAndWorkerNameContaining(startDate, endDate, workerName, pageable);
    }

    public Page<ProductLog> findByWorkDateBetween(Date startDate, Date endDate, Pageable pageable) {
        return productLogRepository.findByWorkDateBetween(startDate, endDate, pageable);
    }
    public ProductLog register(ProductLogDto productLogDto){
        return productLogRepository.save(productLogDto.toEntity());
    }
    public List<String> getProductionTypes(){
        return productLogRepository.findByProductType();
    }
    public ProductLog modifyProductLog(ProductLog productLog) {
        return productLogRepository.save(productLog);
    }

    public List<String> getProductionNumbersByProductionType(String productionType) {
        return productLogRepository.findProductNumbersByProductionType(productionType);
    }
    public List<String> getProductionNamesByProductionNumber(String productionNumber){
        return productLogRepository.findProductNameByProductionNumber(productionNumber);
    }
    public void deleteProduct(Long productId){
        productLogRepository.deleteById(productId);}

    public ProductLog getProductLogById(Long id) {
        Optional<ProductLog> optionalProductLog = productLogRepository.findById(id);
        return optionalProductLog.orElse(null);
    }
    public List<String> getWorkerNames() {
        return workerRepository.findDistinctWorkerNames();
    }
}
