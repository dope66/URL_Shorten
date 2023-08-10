package com.project.UrlJrr.service;

import com.project.UrlJrr.entity.Email;
import com.project.UrlJrr.repository.EmailRepository;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAutoDelete {
    private final EmailRepository emailRepository;

    @Scheduled(initialDelay = 3000, fixedRate = 3600000) // 1시간
    public void emailAutoDelete(){
        System.out.println("이메일 삭제 프로세스");
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Email> emails =emailRepository.findAll();


        for(Email email : emails){
            LocalDateTime dueDate = email.getSendEmailTime().plus(1, ChronoUnit.MONTHS);
            if(currentDateTime.isAfter(dueDate)){
                emailRepository.delete(email);
                System.out.println("이메일 삭제됨 :"+email.getId());
            }

        }
    }

}
