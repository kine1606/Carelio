package com.Carelio.household_service.controller;

import com.Carelio.household_service.dto.request.CreateHouseRequest;
import com.Carelio.household_service.dto.request.UpdateHouseRequest;
import com.Carelio.household_service.dto.response.HouseResponse;
import com.Carelio.household_service.service.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    public ResponseEntity<HouseResponse> create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateHouseRequest request
    )
    {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(houseService.create(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<HouseResponse>> getAll(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(houseService.getAll(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseResponse> getById(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(houseService.getById(userId, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HouseResponse> update(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @RequestBody @Valid UpdateHouseRequest request
    )
    {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(houseService.update(userId,id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HouseResponse> delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(houseService.delete(userId, id));
    }
}
