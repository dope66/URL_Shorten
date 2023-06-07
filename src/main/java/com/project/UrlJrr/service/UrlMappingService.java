package com.project.UrlJrr.service;

import com.project.UrlJrr.domain.UrlMapping;
import com.project.UrlJrr.repository.UrlMappingRepository;
import com.project.UrlJrr.utils.UrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlMappingService {

    private final UrlMappingRepository urlMappingRepository;

    public String generateShortUrl(String originalUrl) {
        // 이전의 url 이 있는지확인, 있으면 단축 url 리턴
        UrlMapping urlMapping = urlMappingRepository.findByOriginalUrl(originalUrl);
        if (urlMapping != null) {
            return urlMapping.getShortUrl();
        }

        String shortUrl = UrlGenerator.generateShortUrl();
        urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setCreatedDate(LocalDateTime.now());
        urlMappingRepository.save(urlMapping);
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            return urlMapping.getOriginalUrl();
        }
        throw new RuntimeException("Invalid short URL: " + shortUrl);
    }
}
