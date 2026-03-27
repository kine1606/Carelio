package com.amigoscode.carelio.user.repository;

import com.amigoscode.carelio.user.entity.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(
            @NotBlank(message = "email is required")
            @Email(message = "email is invalid")
            String email);

    boolean existsByUsername(@NotBlank(message = "username is required") String username);

    List<User> findAllByDeletedFalse();
}