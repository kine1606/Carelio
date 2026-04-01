package com.amigoscode.carelio.user.dto;

import com.amigoscode.carelio.equipment.entity.EquipmentConditionStatus;
import com.amigoscode.carelio.equipment.entity.EquipmentStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest
{
    @NotBlank(message = "email is required")
    @Email(message = "email is invalid")
    private String email;

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "full name is required")
    private String fullName;

    private String avtUrl;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;
}