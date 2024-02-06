package com.project.UrlJrr.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
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
                    authorizeRequests.requestMatchers("/user/modify").authenticated();
                    authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN");
                    authorizeRequests.requestMatchers("/user/register").permitAll();
                    authorizeRequests.requestMatchers("/user/registerProc").permitAll();
                    authorizeRequests.requestMatchers("/user/login").anonymous();
                    authorizeRequests.requestMatchers("/user/logout").authenticated();
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
                                    (request, response, authentication) -> {
                                        System.out.println("authentication: " + authentication.getName());
                                        response.sendRedirect("/");
                                    }
                            )
                            .failureHandler(authenticationFailureHandler).permitAll();

                })
                .logout((logout) -> logout.logoutUrl("/user/logout")
                        .logoutSuccessUrl("/")
                        .permitAll())
                .build();

    }


}
