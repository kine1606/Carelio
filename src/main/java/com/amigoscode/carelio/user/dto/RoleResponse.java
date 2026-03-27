package com.amigoscode.carelio.user.dto;

import com.amigoscode.carelio.user.entity.role.RoleCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse
{
    private Long id;
    private RoleCode roleCode;
}
