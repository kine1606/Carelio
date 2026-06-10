package com.amigoscode.carelio.serviceOrder.mapper;

import com.amigoscode.carelio.serviceOrder.dto.CreateServiceOrderRequest;
import com.amigoscode.carelio.serviceOrder.dto.ServiceOrderResponse;
import com.amigoscode.carelio.serviceOrder.dto.UpdateServiceOrderRequest;
import com.amigoscode.carelio.serviceOrder.entity.ServiceOrder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceOrderMapper {
    ServiceOrder toEntity(UpdateServiceOrderRequest req);
    ServiceOrder toEntity(CreateServiceOrderRequest req);

    ServiceOrderResponse toResponse(ServiceOrder serviceOrder);
    List<ServiceOrderResponse> toResponseList(List<ServiceOrder> serviceOrders);
}
