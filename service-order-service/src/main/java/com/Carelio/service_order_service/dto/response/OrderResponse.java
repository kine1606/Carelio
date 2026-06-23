package com.Carelio.service_order_service.dto.response;

import com.Carelio.service_order_service.client.dto.ServiceSkillCode;
import com.Carelio.service_order_service.entity.ServiceOrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse
{
    private Long id;
    private String userId;
    private Long workerId;

    // snapshot
    private Long houseId;
    private String houseAddressLine;

    private Long roomId;
    private String roomName;

    private Long equipmentId;
    private String equipmentSerialNumber;
    private String equipmentBrand;
    private Long equipmentCategoryId;
    private String equipmentCategoryName;
    private Long serviceSkillId;
    private ServiceSkillCode serviceSkillCode;

    private String title;
    private String description;

    private ServiceOrderStatus status;
    private BigDecimal price;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
