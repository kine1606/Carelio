package com.Carelio.household_service.service;

import com.Carelio.household_service.dto.request.CreateHouseRequest;
import com.Carelio.household_service.dto.request.UpdateHouseRequest;
import com.Carelio.household_service.dto.response.HouseResponse;
import com.Carelio.household_service.entity.House;
import com.Carelio.household_service.mapper.HouseMapper;
import com.Carelio.household_service.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HouseService
{

    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    @CachePut(value = "HOUSE_CACHE", key = "#result.id")
    public HouseResponse create(String userId, CreateHouseRequest request)
    {
        House house = houseMapper.toEntity(request);
        house.setUserId(userId);

        House saved = houseRepository.save(house);
        log.info("House {} is saved successfully", saved.getId());
        return houseMapper.toResponse(saved);
    }

    public List<HouseResponse> getAll(String userId) {
        List<House> houses = houseRepository.findByUserIdAndIsDeletedFalse(userId);
        return houseMapper.toResponseList(houses);
    }

    public Page<HouseResponse> getAllWithPagination(String userId, Pageable pageable)
    {
        Page<House> houses = houseRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
        return houses.map(houseMapper::toResponse);
    }

    @Cacheable(value ="HOUSE_CACHE", key = "#houseId")
    public HouseResponse getById(String userId, Long houseId) {
        House house = houseRepository.findByIdAndUserId(houseId, userId)
                .orElseThrow(() -> new RuntimeException("House not found with id: " + houseId));

        return houseMapper.toResponse(house);
    }

    @CachePut(value = "HOUSE_CACHE", key = "#result.id")
    public HouseResponse update(String userId, Long houseId, UpdateHouseRequest request)
    {
        House house = houseRepository.findByIdAndUserId(houseId, userId)
                .orElseThrow(() -> new RuntimeException("House not found with id: " + houseId));
        if(request.getAddressLine() != null) house.setAddressLine(request.getAddressLine());
        House saved = houseRepository.save(house);
        log.info("House {} is updated successfully", saved.getId());
        return houseMapper.toResponse(saved);
    }

    @CacheEvict(value = "HOUSE_CACHE", key = "#houseId")
    public HouseResponse delete(String userId, Long houseId)
    {
        House house = houseRepository.findByIdAndUserId(houseId, userId)
                .orElseThrow(() -> new RuntimeException("House not found with id: " + houseId));
        house.setDeleted(true);
        House saved = houseRepository.save(house);
        log.info("House {} is deleted successfully", saved.getId());
        return houseMapper.toResponse(saved);
    }
}