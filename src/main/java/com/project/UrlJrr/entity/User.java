package com.project.UrlJrr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String skillStack;  // 기술 스택
    private String experience;  // 경력 여부
    private String requirement;  // 학력
    private String roles;
}
