package com.Carelio.household_service.config;

import com.Carelio.household_service.entity.House;
import com.Carelio.household_service.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Order(1)
public class HouseDataSeeder implements CommandLineRunner
{
    private final HouseRepository houseRepository;

    @Override
    public void run(String... args)
    {
        if (houseRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        List<House> houses = List.of(
                House.builder()
                        .userId(1L)
                        .addressLine("Default address for user 1")
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),
                House.builder()
                        .userId(2L)
                        .addressLine("Default address for user 2")
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );

        houseRepository.saveAll(houses);
    }
}
