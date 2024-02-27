package com.project.UrlJrr.dto;

import com.project.UrlJrr.entity.ProductionTest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionTestDto {

    private String processName;
    private String equipmentName;
    private String processWorker;
    private String company;
    private String note;
    private int productionCount;
    private int defectCount;
    private Date productionDate;
    private LocalDateTime registerDate;
    public ProductionTest toEntity() {
        // 현재 날짜의 연, 월, 일만 가져와서 자정 시간으로 설정
        LocalDate now = LocalDate.now();
        ZonedDateTime zonedDateTime = now.atStartOfDay(ZoneId.systemDefault());
        Date productionDateNow = Date.from(zonedDateTime.toInstant());

        return ProductionTest.builder()
                .processName(processName)
                .equipmentName(equipmentName)
                .processWorker(processWorker)
                .company(company)
                .note(note)
                .defectCount(defectCount)
                .productionDate(productionDateNow) // 현재 날짜의 연, 월, 일만 저장
                .registerDate(LocalDateTime.now())
                .productionCount(productionCount)
                .build();
    }

}
