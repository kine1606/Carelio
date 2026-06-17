package com.Carelio.service_order_service.dto.response;

import com.Carelio.service_order_service.entity.UploadedByType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderAttachmentResponse
{
    private Long id;
    private Long orderId;
    private String fileUrl;
    private String fileType;
    private UploadedByType uploadedBy;
    private LocalDateTime createdAt;
}