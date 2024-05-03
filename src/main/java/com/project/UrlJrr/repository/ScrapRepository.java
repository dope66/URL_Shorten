package com.project.UrlJrr.repository;

<<<<<<< HEAD
import com.project.UrlJrr.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap,Long>{

    Optional<Scrap> findById(Long aLong);

    boolean existsByArticleUrl(String articleUrl);
}
=======
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

<<<<<<< HEAD
}
>>>>>>> 51a2909ee3daae4d46c9e325020b144458a80f17
=======
    Page<Scrap> findByArticleTextContaining(String title, Pageable pageable);
}
>>>>>>> main
