package com.project.UrlJrr.dto;

import com.project.UrlJrr.mesenum.ProcessType;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProcessWorkerDto {
    private ProcessType processName; // 공정명
    private String nation; // 국적
    private String position; // 직책
    private String workShift; // 주야간
    private String workerName; // 이름
    private String equipmentName; // 호기

}
