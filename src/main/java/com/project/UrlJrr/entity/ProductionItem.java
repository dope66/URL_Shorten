package com.project.UrlJrr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "productionItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ManagementItem> managementItems = new ArrayList<>();
}
