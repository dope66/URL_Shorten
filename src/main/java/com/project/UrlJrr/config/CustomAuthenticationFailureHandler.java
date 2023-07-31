package com.project.UrlJrr.config;

import com.project.UrlJrr.entity.User;
import com.project.UrlJrr.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final UserRepository userRepository;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");

        // 아이디 검증
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            String errorMessage = "등록되지 않은 아이디입니다.";
            request.getSession().setAttribute("failedUsername", username);
            response.sendRedirect("/user/login?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
            return;
        }

        User user = userOptional.get();

        // 비밀번호 검증
        if (!isPasswordValid(user, request.getParameter("password"))) {
            String errorMessage = "비밀번호가 일치하지 않습니다.";
            request.getSession().setAttribute("failedUsername", username);
            response.sendRedirect("/user/login?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8.toString()));
            return;
        }


    }

    private boolean isPasswordValid(User user, String password) {
        return user.getPassword().equals(password);
    }



}
