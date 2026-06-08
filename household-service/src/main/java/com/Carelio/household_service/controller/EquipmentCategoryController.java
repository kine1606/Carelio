package com.Carelio.household_service.controller;


import com.Carelio.household_service.dto.request.CreateCategoryRequest;
import com.Carelio.household_service.dto.response.CategoryResponse;
import com.Carelio.household_service.service.EquipmentCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/equipment-category")
@RequestMapping
@RequiredArgsConstructor
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
    public CategoryResponse createCategory(CreateCategoryRequest req)
    {
        return equipmentCategoryService.createCategory(req);
    }

//    // not that good.
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id)
//    {
//        equipmentCategoryService.delete(id);
//    }
}
