package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.ProductLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductLogRepository extends JpaRepository<ProductLog, Long> {
    Optional<ProductLog> findById(Long aLong);

    Page<ProductLog> findByWorkerNameContaining(String workerName, Pageable pageable);


    Page<ProductLog> findByWorkDateBetweenAndWorkerNameContaining(Date startDate, Date endDate, String workerName, Pageable pageable);

    Page<ProductLog> findByWorkDateBetween(Date startDate, Date endDate, Pageable pageable);

    @Query("SELECT DISTINCT productionNumber FROM ProductLog WHERE productionType = :productionType")
    List<String> findProductNumbersByProductionType(@Param("productionType") String productionType);

    @Query("SELECT DISTINCT productionName FROM ProductLog WHERE productionType = :productionType")
    List<String> findProductNameByProductionType(@Param("productionType") String productionType);
    @Query("SELECT DISTINCT productionType FROM ProductLog")
    List<String> findByProductType();
}
