package com.amigoscode.carelio.user.dto;

import com.amigoscode.carelio.base.BaseResponse;
import com.amigoscode.carelio.equipment.entity.EquipmentConditionStatus;
import com.amigoscode.carelio.equipment.entity.EquipmentStatus;
import com.amigoscode.carelio.user.entity.role.RoleCode;
import com.amigoscode.carelio.user.entity.role.UserRole;
import com.amigoscode.carelio.user.entity.user.UserStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.List;

@JsonPropertyOrder({
        "id",
        "username",
        "userStatus",
        "roleCodes",
        "createdAt",
        "updatedAt"
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends BaseResponse
{
    private String username;
    private UserStatus userStatus;
    private List<RoleCode> roleCodes;
}