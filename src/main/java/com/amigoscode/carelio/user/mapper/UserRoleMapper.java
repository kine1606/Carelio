package com.amigoscode.carelio.user.mapper;

import com.amigoscode.carelio.user.dto.UserRoleResponse;
import com.amigoscode.carelio.user.entity.role.UserRole;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")

public interface UserRoleMapper
{
    UserRoleResponse toResponse(UserRole userRole);
}
