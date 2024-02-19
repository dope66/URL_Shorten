package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.ProductionTestDto;
import com.project.UrlJrr.entity.ProductionTest;
import com.project.UrlJrr.repository.ProductionTestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductionTestService {
    private final ProductionTestRepository productionTestRepository;

    public ProductionTest register(ProductionTestDto productionTestDto) {
        return productionTestRepository.save(productionTestDto.toEntity());
    }
}
