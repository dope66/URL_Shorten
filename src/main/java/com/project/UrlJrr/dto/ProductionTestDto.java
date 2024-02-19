package com.project.UrlJrr.dto;

import com.project.UrlJrr.entity.ProductionTest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionTestDto {

    private String processName;
    private String equipmentName;
    private String processWorker;
    private String testOne;
    private String testTwo;

    public ProductionTest toEntity() {
        return ProductionTest.builder()
                .processName(processName)
                .equipmentName(equipmentName)
                .processWorker(processWorker)
                .testOne(testOne)
                .testTwo(testTwo)

                .build();
    }

}
