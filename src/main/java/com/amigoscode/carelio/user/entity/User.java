package com.amigoscode.carelio.user.entity;

import com.amigoscode.carelio.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity
{
    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;
    private String avt_url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;
}
