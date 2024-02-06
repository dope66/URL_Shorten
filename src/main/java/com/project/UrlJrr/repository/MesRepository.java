package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.ProductLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface MesRepository extends JpaRepository<ProductLog, Long> {
    Optional<ProductLog> findById(Long aLong);

    Page<ProductLog> findByWorkerNameContaining(String workerName, Pageable pageable);


    Page<ProductLog> findByWorkDateBetweenAndWorkerNameContaining(Date startDate, Date endDate, String workerName, Pageable pageable);

    Page<ProductLog> findByWorkDateBetween(Date startDate, Date endDate, Pageable pageable);
}
