package com.amigoscode.carelio.user.repository;

import com.amigoscode.carelio.user.entity.role.RoleCode;
import com.amigoscode.carelio.user.entity.role.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>
{
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);

    boolean existsByUserIdAndRoleRoleCode(Long userId, RoleCode roleRoleCode);

    Optional<Object> findByUserIdAndRoleId(Long userId, Long roleId);
}