package com.amigoscode.carelio.user.entity.user;

import com.amigoscode.carelio.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "user_provider",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"provider", "user_id_from_provider"})
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthProvider extends BaseEntity
{
    @Column(name = "user_id_from_provider", nullable = false)
    private String userIdFromProvider;

    @Column(name = "email_from_provider")
    private String emailFromProvider;

    // change after
    private String providerName;
}