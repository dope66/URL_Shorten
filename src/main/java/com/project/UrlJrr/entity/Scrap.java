package com.project.UrlJrr.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String articleText;
    private String articleUrl;
    private String company;
    private String deadline;
    private String location;
    private String experience;
    private String requirement;
    private String jobType;
    private boolean sent;

    @Builder
    public Scrap(Long id, String articleText, String articleUrl, String company, String deadline, String location, String experience, String requirement, String jobType,boolean sent) {
        this.id = id;
        this.articleText = articleText;
        this.articleUrl = articleUrl;
        this.company = company;
        this.deadline = deadline;
        this.location = location;
        this.experience = experience;
        this.requirement = requirement;
        this.jobType = jobType;
        this.sent = false; // 기본값 설정
    }


}