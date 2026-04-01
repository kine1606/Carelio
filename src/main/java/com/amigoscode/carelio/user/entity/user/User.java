package com.amigoscode.carelio.user.entity.user;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.user.entity.role.UserRole;
import com.amigoscode.carelio.worker.entity.Worker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity
{
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "avatar_url")
    private String avtUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles;

    @OneToOne(mappedBy = "user")
    private Worker worker;

    @OneToMany(mappedBy = "user")
    private List<UserAddressInformation>  userAddressInformations;
}
