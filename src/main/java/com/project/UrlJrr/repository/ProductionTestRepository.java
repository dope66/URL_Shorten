package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.ProductionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionTestRepository extends JpaRepository<ProductionTest, Long> {


}
