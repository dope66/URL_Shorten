package com.project.UrlJrr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicDataDTO {
    private Long id;
    private String number; // 순
    private String vehicleType; // 차종
    private String partNumber; // 품번
    private String partName; // 품명

    // InspectionItem의 각 항목에 대한 컬럼 추가
    private String inspectionItem1;
    private String standard1;
    private String method1;
    private String specification1;
    private String tolerance1;

    private String inspectionItem2;
    private String standard2;
    private String method2;
    private String specification2;
    private String tolerance2;

    private String inspectionItem3;
    private String standard3;
    private String method3;
    private String specification3;
    private String tolerance3;

}
