package com.Carelio.service_order_service.client;

import com.Carelio.service_order_service.client.dto.*;
import com.Carelio.service_order_service.config.FeignClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "household-service",
        contextId = "orderServiceHouseholdClient",
        url = "${household.service.url}",
        fallbackFactory = HouseholdClient.HouseholdClientFallbackFactory.class,
        configuration = FeignClientConfig.class
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
            @RequestParam("roomId") Long roomId,
            @RequestParam("houseId") Long houseId);

    @Component
    @Slf4j
    class HouseholdClientFallbackFactory implements org.springframework.cloud.openfeign.FallbackFactory<HouseholdClient> {
        @Override
        public HouseholdClient create(Throwable cause) {
            return new HouseholdClient() {

                @Override
                public HouseResponse getHouseById(Long houseId) {
                    log.error("Circuit Breaker [getHouseById] activated! Error: {}", cause.getMessage());
                    HouseResponse fallback = new HouseResponse();
                    fallback.setId(houseId);
                    fallback.setAddressLine("Temporary data");
                    return fallback;
                }

                @Override
                public EquipmentResponse getEquipmentById(Long equipmentId) {
                    log.error("Circuit Breaker [getEquipmentById] activated! Error: {}", cause.getMessage());
                    EquipmentResponse fallback = new EquipmentResponse();
                    fallback.setId(equipmentId);
                    fallback.setName("Temporary data");
                    return fallback;
                }

                @Override
                public RoomResponse getRoomById(Long roomId) {
                    log.error(" Circuit Breaker [getRoomById] activated! Error:{}", cause.getMessage());
                    RoomResponse fallback = new RoomResponse();
                    fallback.setId(roomId);
                    fallback.setName("Temporary data");
                    return fallback;
                }

                @Override
                public EquipmentCategoryResponse getEquipmentCategoryById(Long ecid) {
                    log.error("Circuit Breaker [getEquipmentCategoryById]activated! Error: {}", cause.getMessage());
                    EquipmentCategoryResponse fallback = new EquipmentCategoryResponse();
                    fallback.setId(ecid);
                    fallback.setName("Temporary data");
                    return fallback;
                }

                @Override
                public EquipmentValidationResponse validate(Long equipmentId, Long roomId, Long houseId) {
                    log.error("default Circuit Breaker [validate] activated! Error: {}", cause.getMessage());
                    EquipmentValidationResponse fallback = new EquipmentValidationResponse();
                    fallback.setRoomName("Temporary data");
                    fallback.setHouseAddressLine("Temporary data");
                    return fallback;
                }
            };
        }
    }
}