package com.Carelio.household_service.controller;

import com.Carelio.household_service.dto.request.CreateHouseRequest;
import com.Carelio.household_service.dto.request.UpdateHouseRequest;
import com.Carelio.household_service.dto.response.HouseResponse;
import com.Carelio.household_service.service.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    public ResponseEntity<HouseResponse> create(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody @Valid CreateHouseRequest request
    )
    {
        return ResponseEntity.ok(houseService.create(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<HouseResponse>> getAll(
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return ResponseEntity.ok(houseService.getAll(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseResponse> getById(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(houseService.getById(userId, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HouseResponse> update(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long id,
            @RequestBody @Valid UpdateHouseRequest request
    )
    {
        return ResponseEntity.ok(houseService.update(userId,id, request));
    }
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(
//            @RequestHeader("X-USER-ID") Long userId,
//            @PathVariable Long id
//    ) {
//        houseService.delete(userId, id);
//        return ResponseEntity.noContent().build();
//    }
}
