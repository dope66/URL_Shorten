package com.project.UrlJrr.dto;

import com.project.UrlJrr.entity.Monitoring;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringDto {


    private String processName;
    private String equipmentName;
    private String processWorker;
    private int productionCount;
    private Date productionDate;
    private LocalDateTime registerDate;
    private LocalDateTime endDate;
    private String productState = "생산시작";
    public Monitoring toEntity() {
        // 현재 날짜의 연, 월, 일만 가져와서 자정 시간으로 설정
        LocalDate now = LocalDate.now();
        ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.systemDefault());
        Date productionDateNow = Date.from(zonedDateTime.toInstant());

        return Monitoring.builder()
                .processName(processName)
                .equipmentName(equipmentName)
                .processWorker(processWorker)
                .productionDate(productionDateNow) // 현재 날짜의 연, 월, 일만 저장
                .registerDate(LocalDateTime.now())
                .endDate(endDate)
                .productionCount(productionCount)
                .productState(productState)
                .build();
    }
}
