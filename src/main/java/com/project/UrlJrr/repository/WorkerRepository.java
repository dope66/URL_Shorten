package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.ProcessWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<ProcessWorker, Long> {

    
}
