package com.Carelio.household_service.mapper;

import com.Carelio.household_service.dto.request.CreateCategoryRequest;
import com.Carelio.household_service.dto.response.CategoryResponse;
import com.Carelio.household_service.entity.EquipmentCategory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Primary
public class CategoryMapperSpring implements CategoryMapper {

    @Override
    public EquipmentCategory toEntity(CreateCategoryRequest createCategoryRequest) {
        if (createCategoryRequest == null) {
            return null;
        }
        EquipmentCategory entity = new EquipmentCategory();
        entity.setName(createCategoryRequest.getName());
        return entity;
    }

    @Override
    public CategoryResponse toResponse(EquipmentCategory equipmentCategory) {
        if (equipmentCategory == null) {
            return null;
        }
        CategoryResponse dto = new CategoryResponse();
        dto.setId(equipmentCategory.getId());
        dto.setName(equipmentCategory.getName());
        return dto;
    }

    @Override
    public List<CategoryResponse> toResponseList(List<EquipmentCategory> equipmentCategoryList) {
        if (equipmentCategoryList == null) {
            return List.of();
        }
        return equipmentCategoryList.stream()
                .filter(Objects::nonNull)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
