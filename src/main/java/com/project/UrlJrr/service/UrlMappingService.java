package com.project.UrlJrr.service;

import com.project.UrlJrr.domain.UrlMapping;
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
        log.error("service OptionalURL" + urlMappingOptional);
        if (urlMappingOptional.isPresent()) {
            log.error("옵셔널URl가 존재해서  if문을 탔을 경우.");
            UrlMapping urlMapping = urlMappingOptional.get();
            return urlMapping.getShortUrl();
        } else {
            log.error("if문을 타지 않았다.");
            String shortUrl = UrlGenerator.generateShortUrl();
            log.error("service layer shortURL = " + shortUrl);
            UrlMapping newUrlMapping = new UrlMapping();
            newUrlMapping.setOriginalUrl(originalUrl);
            log.error("service layer originalUrl"+originalUrl);
            newUrlMapping.setShortUrl(shortUrl);
            newUrlMapping.setCreatedDate(LocalDateTime.now());
            urlMappingRepository.save(newUrlMapping);
            log.error("==save Success==");
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
