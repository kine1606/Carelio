package com.Carelio.user_service.controller;

import com.Carelio.user_service.dto.request.UpdateUserRequest;
import com.Carelio.user_service.dto.response.UserResponse;
import com.Carelio.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController
{

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal Jwt jwt)
    {
        return ResponseEntity.ok( userService.getOrCreateProfile(jwt));
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@AuthenticationPrincipal Jwt jwt, UpdateUserRequest request)
    {
        return ResponseEntity.ok( userService.updateProfile(jwt, request));
    }
}