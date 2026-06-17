package com.Carelio.household_service.service;

import com.Carelio.household_service.dto.request.CreateHouseRequest;
import com.Carelio.household_service.dto.request.UpdateHouseRequest;
import com.Carelio.household_service.dto.response.HouseResponse;
import com.Carelio.household_service.entity.House;
import com.Carelio.household_service.mapper.HouseMapper;
import com.Carelio.household_service.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public HouseResponse create(Long userId, CreateHouseRequest request)
    {
        House house = houseMapper.toEntity(request);
        house.setUserId(userId);

        House saved = houseRepository.save(house);
        log.info("House {} is saved successfully", saved.getId());
        return houseMapper.toResponse(saved);
    }

    public List<HouseResponse> getAll(Long userId) {
        List<House> houses = houseRepository.findByUserId(userId);
        return houseMapper.toResponseList(houses);
    }

    public HouseResponse getById(Long userId, Long houseId) {
        House house = houseRepository.findByIdAndUserId(houseId, userId)
                .orElseThrow(() -> new RuntimeException("House not found with id: " + houseId));

        return houseMapper.toResponse(house);
    }

    public HouseResponse update(Long userId, Long houseId, UpdateHouseRequest request)
    {
        House house = houseRepository.findByIdAndUserId(houseId, userId)
                .orElseThrow(() -> new RuntimeException("House not found with id: " + houseId));
        if(request.getAddressLine() != null) house.setAddressLine(request.getAddressLine());
        House saved = houseRepository.save(house);
        log.info("House {} is updated successfully", saved.getId());
        return houseMapper.toResponse(saved);
    }

//    public void delete(Long userId, Long houseId) {
//        House house = houseRepository.findById(houseId)
//                .filter(h -> h.getUserId().equals(userId))
//                .orElseThrow(() -> new RuntimeException("House not found"));
//
//        houseRepository.delete(house);
//    }
}