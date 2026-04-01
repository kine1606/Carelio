package com.amigoscode.carelio.user.dto;

import com.amigoscode.carelio.user.entity.role.Role;
import com.amigoscode.carelio.user.entity.role.UserRole;
import com.amigoscode.carelio.user.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleResponse
{
    private RoleResponse role;
    private LocalDateTime grantedAt;
    private Long grantedBy;
}