package com.project.UrlJrr.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests -> {
                    authorizeRequests.requestMatchers("/matching/**").authenticated();
                    authorizeRequests.requestMatchers("/crawling/apply").authenticated();
                    authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN");
                    authorizeRequests.requestMatchers("/user/register").permitAll();
                    authorizeRequests.requestMatchers("/user/registerProc").permitAll();
                    authorizeRequests.requestMatchers("/user/login").permitAll();
                    authorizeRequests.requestMatchers("/user/logout").permitAll();
                    authorizeRequests.requestMatchers("/user/findPassword").permitAll();
                    authorizeRequests.anyRequest().permitAll();
                }))
                .exceptionHandling((httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler);
                }))
                .formLogin((formLogin) -> {
                    /* 권한이 필요한 요청은 해당 url로 리다이렉트 */
                    formLogin
                            .loginPage("/user/login")
                            .loginProcessingUrl("/user/loginProc")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .successHandler(
                                    new AuthenticationSuccessHandler() {
                                        @Override
                                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                            System.out.println("authentication: " + authentication.getName());
                                            response.sendRedirect("/");
                                        }
                                    }
                            )
                            .failureHandler(authenticationFailureHandler)



                            .permitAll();


                })
                .logout((logout) -> {
                    logout.logoutUrl("/user/logout")
                            .logoutSuccessUrl("/")
                            .permitAll();

                })
                .build();

    }


}
