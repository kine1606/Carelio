package com.amigoscode.carelio.equipment.dto;

import com.amigoscode.carelio.base.BaseResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentSimpleResponse extends BaseResponse
{
    private String name;
}