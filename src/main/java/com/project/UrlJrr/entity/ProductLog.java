package com.project.UrlJrr.entity;

import jakarta.persistence.*;
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
    private Long production;  // 생산량
    private Long defectRate; // 불량률
    private String equipmentName; // 호기
    private String workerName; // 담당자

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private ProcessWorker processWorker;


}
