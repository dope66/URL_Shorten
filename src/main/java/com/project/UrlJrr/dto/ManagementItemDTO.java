package com.project.UrlJrr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagementItemDTO {

    private String managementNumber; // 관리번호
    private String managementItem; // 관리항목
    private String specialCharacteristic; // 특별특성
    private String standard; // 기준
    private String verificationMethod; // 확인방법
    private String frequency; // 주기
    private String record; // 기록
    private String actionForAbnormality; // 이상시조치방법

}

