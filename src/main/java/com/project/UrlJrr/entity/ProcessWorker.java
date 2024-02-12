package com.project.UrlJrr.entity;

import com.project.UrlJrr.mesenum.ProcessType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProcessType processName; // 공정명
    private String nation; // 국적
    private String position; // 직책
    private String workShift; // 주야간
    private String workerName; // 이름
    private String equipmentName; // 호기

    @OneToMany(mappedBy = "processWorker")
    private List<ProductLog> productLogs;

}
