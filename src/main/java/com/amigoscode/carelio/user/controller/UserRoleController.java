package com.amigoscode.carelio.user.controller;

import com.amigoscode.carelio.user.dto.UserRoleResponse;
import com.amigoscode.carelio.user.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.amigoscode.carelio.user.entity.role.UserRole;
import com.amigoscode.carelio.user.service.UserRoleService;
import java.util.List;

@RestController
@RequestMapping("/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;
    private final UserRoleMapper userRoleMapper;

    @PostMapping("/{userId}/roles/{roleId}")
    public UserRoleResponse assignRole(
            @PathVariable Long userId, @PathVariable Long roleId, @RequestParam Long grantedBy)
    {
        return userRoleMapper.toResponse(userRoleService.assignRole(userId, roleId, grantedBy));
    }

}
