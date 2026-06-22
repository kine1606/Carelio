package com.Carelio.user_service.dto.response;

import com.Carelio.user_service.entity.UserStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse
{
    private Long id;
    private UUID keycloakUserId;
    private String name;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
