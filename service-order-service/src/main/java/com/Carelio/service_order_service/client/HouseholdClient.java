package com.Carelio.service_order_service.client;

import com.Carelio.service_order_service.client.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "household-service", url = "${household.service.url}")
public interface HouseholdClient
{
    @GetMapping("/api/houses/{houseId}")
    public HouseResponse getHouseById(@PathVariable("houseId") Long houseId);

    @GetMapping("/api/equipments/{equipmentId}")
    public EquipmentResponse getEquipmentById(@PathVariable("equipmentId") Long equipmentId);

    @GetMapping("/api/rooms/{roomId}")
    public RoomResponse getRoomById(@PathVariable("roomId") Long roomId);

    @GetMapping("/api/equipment-category/{ecid}")
    public EquipmentCategoryResponse getEquipmentCategoryById(@PathVariable("ecid") Long ecid);

    @GetMapping("/api/equipments/{equipmentId}/validate")
    public EquipmentValidationResponse validate(@RequestHeader("X-User-Id") Long userId,
                                                @PathVariable("equipmentId") Long equipmentId,
                                                @RequestParam Long roomId,
                                                @RequestParam Long houseId);
}
