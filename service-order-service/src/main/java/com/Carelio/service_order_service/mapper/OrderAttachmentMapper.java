package com.Carelio.service_order_service.mapper;

import com.Carelio.service_order_service.dto.response.OrderAttachmentResponse;
import com.Carelio.service_order_service.entity.OrderAttachment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderAttachmentMapper
{
    OrderAttachmentResponse toResponse(OrderAttachment attachment);

    List<OrderAttachmentResponse> toResponseList(List<OrderAttachment> attachments);
}
