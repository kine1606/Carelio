package com.Carelio.service_order_service.dto.request;

import com.Carelio.service_order_service.entity.UploadedByType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAttachmentRequest
{
    private Long orderId;
    private String fileUrl;
    private String fileType;
    private UploadedByType uploadedBy;
}
