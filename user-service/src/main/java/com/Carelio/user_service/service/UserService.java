package com.Carelio.user_service.service;

import com.Carelio.user_service.dto.request.UpdateUserRequest;
import com.Carelio.user_service.dto.response.UserResponse;
import com.Carelio.user_service.entity.User;
import com.Carelio.user_service.entity.UserStatus;
import com.Carelio.user_service.mapper.UserMapper;
import com.Carelio.user_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    //=========================== CUSTOMER ===================================
    //GET /api/user/profile
    public UserResponse getOrCreateProfile(Jwt jwt)
    {
        String keycloakUserId = jwt.getSubject();
        User user = userRepository.findByKeycloakUserId(keycloakUserId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setKeycloakUserId(keycloakUserId);
                    newUser.setEmail(jwt.getClaimAsString("email"));
                    newUser.setName(jwt.getClaimAsString("preferred_username"));
                    newUser.setStatus(UserStatus.ACTIVE);
                    User saved = userRepository.save(newUser);
                    log.info("Created user with id {} ", saved.getKeycloakUserId());
                    return saved;
                });
        log.info("Get user with id {} ", user.getKeycloakUserId());
        return userMapper.toResponse(user);
    }

    // PATCH /api/users/profile
    public UserResponse updateProfile(Jwt jwt, UpdateUserRequest request)
    {
        User user = userRepository.findByKeycloakUserId(jwt.getSubject())
                .orElseThrow(() -> new EntityNotFoundException("User not found with keycloackUserid " + jwt.getSubject()));
        if(request.getEmail() != null) user.setEmail(request.getEmail());
        if(request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if(request.getAvatarUrl() != null) user.setAvatarUrl(request.getAvatarUrl());
        if(request.getName() != null) user.setName(request.getName());
        User saved = userRepository.save(user);
        log.info("Updated user with id {} ", saved.getKeycloakUserId());
        return  userMapper.toResponse(saved);
    }

}
