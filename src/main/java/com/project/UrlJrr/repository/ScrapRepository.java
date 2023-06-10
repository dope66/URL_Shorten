package com.project.UrlJrr.repository;

import com.project.UrlJrr.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap,Long>{

    Optional<Scrap> findById(Long aLong);

    boolean existsByArticleUrl(String articleUrl);
}
