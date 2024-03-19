package com.project.UrlJrr.dto;

import com.project.UrlJrr.entity.ProcessWorker;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ProcessWorkerDto {
    private Long id;
    private String processName; // 공정명
    private String equipmentName; // 호기
    private String workerName; // 이름
    private String nation; // 국적
    private String position; // 직책
    private String workShift; // 주야간


    private List<ProductLogDto> productLogs;

    private String imagePath; // 이미지 파일 경로 필드 추가

    public ProcessWorker toEntity(ProcessWorkerDto dto) {
        return ProcessWorker.builder()
                .processName(dto.getProcessName())
                .equipmentName(dto.getEquipmentName())
                .workerName(dto.getWorkerName())
                .nation(dto.getNation())
                .position(dto.getPosition())
                .workShift(dto.getWorkShift())
                .imagePath(dto.getImagePath()) // 이미지 경로 정보를 엔티티에 설정
                .build();
    }

}
