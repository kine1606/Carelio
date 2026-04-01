package com.amigoscode.carelio.user.service;

import com.amigoscode.carelio.user.entity.role.Role;
import com.amigoscode.carelio.user.entity.role.RoleCode;
import com.amigoscode.carelio.user.entity.user.User;
import com.amigoscode.carelio.user.repository.RoleRepository;
import com.amigoscode.carelio.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import com.amigoscode.carelio.user.entity.role.UserRole;
import com.amigoscode.carelio.user.repository.UserRoleRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRoleService
{

    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;

    private void validateAdminPermission(Long grantedBy)
    {
        boolean isAdmin = userRoleRepository.existsByUserIdAndRoleRoleCode(grantedBy, RoleCode.ADMIN);
        if(!isAdmin) throw new RuntimeException("Only ADMIN can do this");
    }

    @Transactional
    public UserRole assignRole(Long userId, Long roleId, Long grantedBy)
    {
        validateAdminPermission(grantedBy);
        User user = userService.getById(userId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        boolean exists = userRoleRepository.existsByUserIdAndRoleId(userId, roleId);
        if (exists)  throw new RuntimeException("User is already assigned this role");

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setGrantedAt(LocalDateTime.now());
        userRole.setGrantedBy(grantedBy);
        return userRoleRepository.save(userRole);
    }
//    @Transactional
//    public UserRole removeRole(Long userId, Long roleId, Long remoBy)
//    {
//        validateAdminPermission(grantedBy);
//        User user = userService.getById(userId);
//        Role role = roleRepository.findById(roleId)
//                .orElseThrow(() -> new RuntimeException("Role not found"));
//        boolean exists = userRoleRepository.existsByUserIdAndRoleId(userId, roleId);
//        if (!exists)  throw new RuntimeException("User doesn't have this role to remove");
//        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId)
//                .orElseThrow(() -> new RuntimeException("User role not found"));
//
//        if (userId.equals(removedBy) && userRole.getRole().getRoleCode() == RoleCode.ADMIN) {
//            throw new RuntimeException("Admin cannot remove ADMIN role from themselves");
//        }
//
//        userRoleRepository.delete(userRole);
//    }
}