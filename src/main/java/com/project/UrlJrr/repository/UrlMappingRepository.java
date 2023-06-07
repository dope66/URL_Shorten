package com.project.UrlJrr.repository;

import com.project.UrlJrr.domain.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findFirstById(Long id);
    UrlMapping findByOriginalUrl(String originalUrl);
    UrlMapping findByShortUrl(String shortUrl);
}
