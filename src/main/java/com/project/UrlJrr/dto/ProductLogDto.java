package com.project.UrlJrr.dto;

import com.project.UrlJrr.entity.ProductLog;
import lombok.*;

import java.util.Date;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLogDto {



    private String productionType; //차종
    private String productionNumber; // 품번
    private String productionName; // 품명
    private Long production;  // 생산량
    private Long defectRate; // 불량률
    private String equipmentName; // 담당호기
    private String workerName; // 담당자
    private Date workDate;      // 일한 날짜

    public ProductLog toEntity() {
        return ProductLog.builder()
                .workerName(workerName)
                .productionType(productionType)
                .production(production)
                .productionNumber(productionNumber)
                .workDate(workDate)
                .productionName(productionName)
                .equipmentName(equipmentName)
                .defectRate(defectRate)
                .build();
    }


}
