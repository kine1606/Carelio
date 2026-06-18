package com.Carelio.service_order_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest
{
//    @NotNull
    private Long userId;
//    @NotNull
    private Long houseId;
//    @NotNull
    private Long roomId;
//    @NotNull
    private Long equipmentId;
//    @NotNull
    private Long serviceSkillId;
//    @NotNull
    private Long equipmentCategoryId;

    private String title;
    private String description;
}
