package com.Carelio.service_order_service.mapper;


import com.Carelio.service_order_service.client.dto.EquipmentValidationResponse;
import com.Carelio.service_order_service.client.dto.ServiceSkillResponse;
import com.Carelio.service_order_service.dto.request.OrderRequest;
import com.Carelio.service_order_service.dto.response.OrderResponse;
import com.Carelio.service_order_service.entity.Order;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
    // Build Order from request + userId + validated snapshots
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "userId", target = "userId")
    // IDs from request
    @Mapping(source = "orderRequest.houseId", target = "houseId")
    @Mapping(source = "orderRequest.roomId", target = "roomId")
    @Mapping(source = "orderRequest.equipmentId", target = "equipmentId")
    @Mapping(source = "orderRequest.serviceSkillId", target = "serviceSkillId")
    @Mapping(source = "orderRequest.equipmentCategoryId", target = "equipmentCategoryId")
    // Basic info from request
    @Mapping(source = "orderRequest.title", target = "title")
    @Mapping(source = "orderRequest.description", target = "description")
    // Snapshots from equipment validation
    @Mapping(source = "equipmentInfo.houseAddressLine", target = "houseAddressLine")
    @Mapping(source = "equipmentInfo.roomName", target = "roomName")
    @Mapping(source = "equipmentInfo.equipmentSerialNumber", target = "equipmentSerialNumber")
    @Mapping(source = "equipmentInfo.equipmentBrand", target = "equipmentBrand")
    // Snapshots from skill/category validation
    @Mapping(source = "equipmentInfo.equipmentCategoryName", target = "equipmentCategoryName")
    @Mapping(source = "skillInfo.serviceSkillCode", target = "serviceSkillCode")
    Order toEntity(OrderRequest orderRequest,
                   Long userId,
                   EquipmentValidationResponse equipmentInfo,
                   ServiceSkillResponse skillInfo);
}
