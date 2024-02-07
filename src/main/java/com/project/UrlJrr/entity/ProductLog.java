package com.project.UrlJrr.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productionType; //차종
    private String productionNumber; // 품번
    private Date workDate;      // 일한 날짜
    private String productionName; // 품명
    private String production;  // 생산량
    private String defectRate; // 불량률
    private String equipmentName; // 담당호기
    private String workerName; // 담당자


}
