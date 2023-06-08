package com.project.UrlJrr.repository;

import com.project.UrlJrr.domain.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional <UrlMapping> findFirstById(Long id);
    Optional <UrlMapping> findByOriginalUrl(String originalUrl);
    Optional <UrlMapping> findByShortUrl(String shortUrl);
}
