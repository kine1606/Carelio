package com.Carelio.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Bật tính năng check @PreAuthorize("hasRole('...')") sau này
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF cho API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll() // API công khai không cần login
                        .anyRequest().authenticated() // Tất cả các API còn lại bắt buộc phải có Token hợp lệ
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {})); // Bật Resource Server đọc JWT

        return http.build();
    }
}