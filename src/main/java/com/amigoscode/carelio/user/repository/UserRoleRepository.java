package com.amigoscode.carelio.user.repository;

import com.amigoscode.carelio.user.entity.role.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}