package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.BasicData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasicDataRepository extends JpaRepository<BasicData,Long> {
}
