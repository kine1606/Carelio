package com.Carelio.household_service.controller;


import com.Carelio.household_service.dto.request.CreateCategoryRequest;
import com.Carelio.household_service.dto.response.CategoryResponse;
import com.Carelio.household_service.service.EquipmentCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/equipment-category")
@RequiredArgsConstructor
@Slf4j
public class EquipmentCategoryController
{
    private final EquipmentCategoryService equipmentCategoryService;

    @GetMapping
    public List<CategoryResponse> getAll()
    {
        return equipmentCategoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(@PathVariable Long id)
    {
        return equipmentCategoryService.getById(id);
    }

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(
            @AuthenticationPrincipal Jwt jwt,
            CreateCategoryRequest req)
    {
        String userId = jwt.getSubject();
        CategoryResponse categoryResponse = equipmentCategoryService.createCategory(req);
        log.info("Created equipment category with id {} by userId {}", categoryResponse.getId(), userId);
        return ResponseEntity.ok(categoryResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @RequestBody CreateCategoryRequest request)
    {
        String userId = jwt.getSubject();
        CategoryResponse categoryResponse = equipmentCategoryService.updateCategory(id, request);
        log.info("update equipment category with id {} by userId {}", categoryResponse.getId(), userId);
        log.info("updated time: {}" , LocalDateTime.now());
        return ResponseEntity.ok(categoryResponse);
    }
}
