package com.Carelio.service_order_service.dto.response;

import com.Carelio.service_order_service.entity.ServiceOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse
{
    private Long id;
    private Long userId;
    private Long workerId;
    private AddressResponse addressResponse;
    private Long roomId;
    private Long equipmentId;
    private Long serviceSkillId;
    private Long equipmentCategoryId;
    private String title;
    private String description;
    private ServiceOrderStatus status;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
