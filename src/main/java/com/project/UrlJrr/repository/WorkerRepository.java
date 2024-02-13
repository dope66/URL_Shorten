package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.ProcessWorker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<ProcessWorker, Long> {


    @Query("SELECT DISTINCT pw.workerName FROM ProcessWorker pw")
    List<String> findDistinctWorkerNames();


    Page<ProcessWorker> findByWorkerNameContaining(String workerName, Pageable pageable);
}
