package com.project.UrlJrr.repository;

import com.project.UrlJrr.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap,Long>{

    Optional<Scrap> findById(Long aLong);
    
    boolean existsByArticleUrl(String articleUrl);

    List<Scrap> findBySent(boolean b);

    List<Scrap> findByCreateDate(String createDate);

    List<Scrap> findByDeadlineBefore(String deadline);
}