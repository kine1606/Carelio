package com.Carelio.service_order_service.client;

import com.Carelio.service_order_service.client.dto.*;
import com.Carelio.service_order_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "household-service",
        contextId = "orderServiceHouseholdClient",
        url = "${household.service.url}",
        configuration = FeignClientConfig.class // <-- 1. NẠP CẤU HÌNH INTERCEPTOR VÀO ĐÂY
)
public interface HouseholdClient {

    @GetMapping("/api/houses/{houseId}")
    HouseResponse getHouseById(@PathVariable("houseId") Long houseId);

    @GetMapping("/api/equipments/{equipmentId}")
    EquipmentResponse getEquipmentById(@PathVariable("equipmentId") Long equipmentId);

    @GetMapping("/api/rooms/{roomId}")
    RoomResponse getRoomById(@PathVariable("roomId") Long roomId);

    @GetMapping("/api/equipment-category/{ecid}")
    EquipmentCategoryResponse getEquipmentCategoryById(@PathVariable("ecid") Long ecid);

    @GetMapping("/api/equipments/{equipmentId}/validate")
    EquipmentValidationResponse validate(
            @PathVariable("equipmentId") Long equipmentId,
            @RequestParam Long roomId,
            @RequestParam Long houseId);
}