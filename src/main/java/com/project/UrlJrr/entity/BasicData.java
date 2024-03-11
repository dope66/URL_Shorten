package com.project.UrlJrr.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
public class BasicData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number; // 순
    private String vehicleType; // 차종
    private String partNumber; // 품번
    private String partName; // 품명
    // 검사 항목을 리스트로 관리
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "basicDataId") // 이 이름은 외래키 컬럼의 이름입니다.
    private List<InspectionItem> inspectionItems;


}
