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


    private String workerName ; // 작업자
    private String position;    // 직무
    private String workload;  // 작업량
    private String productNumber; // 품번
    private Date workDate;      // 일한 날짜
    private String productName; // 품명


    public ProductLog toEntity() {
        return ProductLog.builder()
                .workerName(workerName)
                .position(position)
                .workload(workload)
                .productNumber(productNumber)
                .workDate(workDate)
                .productName(productName)
                .build();
    }


}
