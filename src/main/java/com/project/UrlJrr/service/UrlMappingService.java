package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.UrlMapping;
import com.project.UrlJrr.repository.UrlMappingRepository;
import com.project.UrlJrr.utils.UrlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;


    public String generateShortUrl(String originalUrl) {
        Optional<UrlMapping> urlMappingOptional = urlMappingRepository.findByOriginalUrl(originalUrl);
        if (urlMappingOptional.isPresent()) {
            UrlMapping urlMapping = urlMappingOptional.get();
            return urlMapping.getShortUrl();
        } else {
            String shortUrl = UrlGenerator.generateShortUrl();
            UrlMapping newUrlMapping = new UrlMapping();
            newUrlMapping.setOriginalUrl(originalUrl);
            newUrlMapping.setShortUrl(shortUrl);
            newUrlMapping.setCreatedDate(LocalDateTime.now());
            urlMappingRepository.save(newUrlMapping);
            return shortUrl;
        }
    }

    public String getOriginalUrl(String shortUrl) {
        if ("favicon.ico".equals(shortUrl)) {
            return ""; // 빈 문자열 반환 또는 다른 처리 방식 선택
        }
        Optional<UrlMapping> urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping.isPresent()) {
            return urlMapping.get().getOriginalUrl();
        }
        throw new RuntimeException("Invalid short URL: " + shortUrl);
    }

}
