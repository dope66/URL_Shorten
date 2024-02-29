package com.project.UrlJrr.service;

import com.project.UrlJrr.dto.ProductionTestDto;
import com.project.UrlJrr.entity.ProductionTest;
import com.project.UrlJrr.repository.ProductionTestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductionTestService {
    private final ProductionTestRepository productionTestRepository;

    public ProductionTest register(ProductionTestDto productionTestDto) {
        return productionTestRepository.save(productionTestDto.toEntity());
    }

    public List<ProductionTest> findAll() {
        return productionTestRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }
}
