package com.project.UrlJrr.entity;

import com.project.UrlJrr.mesenum.ProcessType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessWorker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProcessType processName;
    private String nation;
    private String position;
    private String workShift;
    private String workerName;
    private String equipmentName;

    private String imagePath; // 이미지 파일 경로 저장 필드 추가

    @OneToMany(mappedBy = "processWorker")
    private List<ProductLog> productLogs;

}
