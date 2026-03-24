package com.amigoscode.carelio.user.entity.role;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRoleId implements Serializable
{
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "role_id", nullable = false)
    private Long roleId;
}
