package com.project.UrlJrr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ManagementItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String managementNumber; // 관리번호
    private String managementItem; // 관리항목
    private String specialCharacteristic; // 특별특성
    private String standard; // 기준
    private String verificationMethod; // 확인방법
    private String frequency; // 주기
    private String record; // 기록
    private String actionForAbnormality; // 이상시조치방법


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_item_id")
    private ProductionItem productionItem;
}
