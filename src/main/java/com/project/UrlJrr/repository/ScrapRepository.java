package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap,Long>{
    Optional<Scrap> findById(Long aLong);
    List<Scrap> findBySent(boolean b);

    Page<Scrap> findAll(Pageable pageable);

    Page<Scrap> findByArticleTextContaining(String title, Pageable pageable);
}