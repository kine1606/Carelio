package com.amigoscode.carelio.user.dto;

import com.amigoscode.carelio.user.entity.role.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleResponse {

    @JsonIgnore
    private UserResponse user;

    private Long roleId;

    private Long grantedBy;
}