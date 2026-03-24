package com.amigoscode.carelio.user.entity.role;

import com.amigoscode.carelio.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity
{
    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false)
    private RoleCode roleCode;

    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles;
}
