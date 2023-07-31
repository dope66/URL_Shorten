package com.project.UrlJrr.dto;

import com.project.UrlJrr.entity.UrlMapping;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class UrlMappingDto {
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime createdDate;

    public UrlMapping toEntity(){
        return UrlMapping.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortUrl)
                .build();
    }

}
