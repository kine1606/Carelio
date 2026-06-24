package com.Carelio.service_order_service.mapper;

import com.Carelio.service_order_service.dto.response.OrderReviewResponse;
import com.Carelio.service_order_service.entity.OrderReview;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderReviewMapper
{
    OrderReviewResponse toResponse(OrderReview review);
    List<OrderReviewResponse> toResponseList(List<OrderReview> reviews);
}
