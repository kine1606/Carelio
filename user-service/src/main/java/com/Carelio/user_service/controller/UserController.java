package com.Carelio.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> userInfo = new HashMap<>();

        // Lấy thông tin ID của User từ trường "sub" trong Token
        userInfo.put("keycloakUserId", jwt.getSubject());

        // Lấy tên tài khoản
        userInfo.put("username", jwt.getClaimAsString("preferred_username"));

        // Lấy email
        userInfo.put("email", jwt.getClaimAsString("email"));

        return ResponseEntity.ok(userInfo);
    }
}