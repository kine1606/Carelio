package com.amigoscode.carelio.user.service;

import com.amigoscode.carelio.user.entity.role.Role;
import com.amigoscode.carelio.user.entity.user.User;
import com.amigoscode.carelio.user.repository.RoleRepository;
import com.amigoscode.carelio.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.amigoscode.carelio.user.entity.role.UserRole;
import com.amigoscode.carelio.user.repository.UserRoleRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UserRole assignRole(Long userId, Long roleId, Long grantedBy)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        boolean exists = userRoleRepository.existsByUserIdAndRoleId(userId, roleId);
        if(exists)
        {
            throw new RuntimeException("User is already assigned this role");
        }

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setGrantedAt(LocalDateTime.now());
        userRole.setGrantedBy(grantedBy);
        return userRoleRepository.save(userRole);
    }
}