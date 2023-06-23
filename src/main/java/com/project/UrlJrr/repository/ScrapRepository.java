package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.Scrap;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap,Long>{
    Optional<Scrap> findById(Long aLong);
    List<Scrap> findBySent(boolean b);
    @Query("SELECT s FROM Scrap s ORDER BY s.id DESC")
    Page<Scrap> findAllByOrderByidDesc(Pageable pageable);

    Page<Scrap> findByArticleTextContainingAndCompanyContaining (String articleText,String company,Pageable pageable);



}