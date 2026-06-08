package com.Carelio.household_service.service;


import com.Carelio.household_service.dto.request.CreateCategoryRequest;
import com.Carelio.household_service.dto.response.CategoryResponse;
import com.Carelio.household_service.entity.EquipmentCategory;
import com.Carelio.household_service.mapper.CategoryMapperSpring;
import com.Carelio.household_service.repository.EquipmentCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EquipmentCategoryService
{
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final CategoryMapperSpring categoryMapper;

    public List<CategoryResponse> getAll()
    {
        return categoryMapper.toResponseList( equipmentCategoryRepository.findAll());
    }

    public CategoryResponse getById(Long id)
    {
        EquipmentCategory ec = equipmentCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment category not found with id: " + id));
        return categoryMapper.toResponse(ec);
    }

    public CategoryResponse createCategory(CreateCategoryRequest req)
    {
        EquipmentCategory ec = categoryMapper.toEntity(req);
        EquipmentCategory saved = equipmentCategoryRepository.save(ec);
        log.info("Category created  with id: {}", saved.getId());
        return categoryMapper.toResponse(saved);
    }


//    public void delete(Long id)
//    {
//        EquipmentCategory ec = getById(id);
//        if (!ec.getEquipments().isEmpty()) {
//            throw new RuntimeException("Equipment category has equipment(s)");
//        }
//        equipmentCategoryRepository.delete(ec);
//    }
}
