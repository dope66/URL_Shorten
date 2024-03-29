package com.project.UrlJrr.dto;

import com.project.UrlJrr.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String username;
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
    private String password2;
    private String skillStack;
    private String experience;
    private String email;
    private boolean subScribe=false;
    private String roles ;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .skillStack(skillStack)
                .experience(experience)
                .email(email)
                .roles(roles)
                .build();
    }


}
