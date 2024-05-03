package com.project.UrlJrr.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProductionItemDTO {
    private Long id;
    private String number; // 순  제거해도됨
    private String productionLine; // 생산라인
    private String vehicleType; // 차종
    private String partNumber; // 품번
    private String partName; // 품명
    private String productionFacility; // 생산설비
    private String processName; // 공정명
    private String processNumber; // 공정번호
    private String equipmentName; // 설비명
    private String equipmentNumber; // 설비번호


    private List<ManagementItemDTO> managementItems;

}
