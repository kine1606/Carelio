package com.Carelio.user_service.dto.request;

import com.Carelio.user_service.entity.UserStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest
{
    private String name;
    private String email;
    private String phoneNumber;
    private String avatarUrl;
    private UserStatus status;
}
