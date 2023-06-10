package com.project.UrlJrr.service;

import com.project.UrlJrr.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveScrapService {

    private final ScrapingService scrapingService;
    private final ScrapRepository scrapRepository;

    public void saveScrap( String article,String articleText){

    }



}
