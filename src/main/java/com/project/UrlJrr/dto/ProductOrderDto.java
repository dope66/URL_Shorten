package com.project.UrlJrr.dto;

import com.project.UrlJrr.entity.ProductionOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderDto {
    private String processName;
    private Date productionDate;
    private String registerDate;
    private String processWorker;
    private String equipmentName;
    private int productionCount;
    private String note;

    public ProductionOrder toEntity() {
        return ProductionOrder.builder()
                .processName(processName)
                .productionDate(productionDate)
                .registerDate(registerDate)
                .processWorker(processWorker)
                .equipmentName(equipmentName)
                .productionCount(productionCount)
                .note(note)
                .build();
    }

}
