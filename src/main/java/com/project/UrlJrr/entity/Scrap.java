package com.project.UrlJrr.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String sourceSite;
    @Column(length = 1000)
    private String skillStack;
    private String company;
    private String deadline;
    private String location;
    private String experience;
    private String requirement;
    private String jobType;
    private boolean sent;
    private String createDate;



    @Builder
    public Scrap(Long id, String articleText, String articleUrl, String sourceSite,String skillStack,
                 String company, String deadline, String location, String experience, String requirement,
                 String jobType, boolean sent, String createDate) {
        this.id = id;
        this.articleText = articleText;
        this.articleUrl = articleUrl;
        this.sourceSite = sourceSite;
        this.skillStack = skillStack;
        this.company = company;
        this.deadline = deadline;
        this.location = location;
        this.experience = experience;
        this.requirement = requirement;
        this.jobType = jobType;
        this.sent = sent;
        this.createDate = createDate;
    }


}