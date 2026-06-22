package com.Carelio.user_service.controller;

import com.Carelio.user_service.dto.response.UserResponse;
import com.Carelio.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController
{

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getMyProfile(@AuthenticationPrincipal Jwt jwt)
    {
        return ResponseEntity.ok( userService.getOrCreateProfile(jwt));
    }

//    @PatchMapping
//    public ResponseEntity<Map<String, Object>> updateUser(@AuthenticationPrincipal Jwt jwt,
//                                                          @RequestBody Map<String, Object> userInfo)
//    {
//
//    }
}