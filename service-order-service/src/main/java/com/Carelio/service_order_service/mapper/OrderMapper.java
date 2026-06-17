package com.Carelio.service_order_service.mapper;


import com.Carelio.service_order_service.dto.request.OrderRequest;
import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);

    Order toEntity(OrderRequest orderRequest);
}
