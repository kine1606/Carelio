package com.Carelio.household_service.mapper;

import com.Carelio.household_service.dto.request.CreateCategoryRequest;
import com.Carelio.household_service.dto.response.CategoryResponse;
import com.Carelio.household_service.entity.EquipmentCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper
{
    EquipmentCategory toEntity(CreateCategoryRequest createCategoryRequest);

    CategoryResponse toResponse(EquipmentCategory equipmentCategory);

    List<CategoryResponse> toResponseList(List<EquipmentCategory> equipmentCategoryList);
}
