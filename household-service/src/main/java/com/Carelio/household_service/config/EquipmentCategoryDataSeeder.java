package com.Carelio.household_service.config;

import com.Carelio.household_service.entity.EquipmentCategory;
import com.Carelio.household_service.repository.EquipmentCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Order(1)
public class EquipmentCategoryDataSeeder implements CommandLineRunner {

    private final EquipmentCategoryRepository equipmentCategoryRepository;

    @Override
    public void run(String... args) {
        if (equipmentCategoryRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        List<EquipmentCategory> categories = List.of(
                EquipmentCategory.builder()
                        .name("Máy lạnh")
                        .createdAt(now)
                        .updatedAt(now)
                        .deleted(false)
                        .build(),

                EquipmentCategory.builder()
                        .name("Tivi")
                        .createdAt(now)
                        .updatedAt(now)
                        .deleted(false)
                        .build(),

                EquipmentCategory.builder()
                        .name("Tủ lạnh")
                        .createdAt(now)
                        .updatedAt(now)
                        .deleted(false)
                        .build(),

                EquipmentCategory.builder()
                        .name("Máy giặt")
                        .createdAt(now)
                        .updatedAt(now)
                        .deleted(false)
                        .build()
        );

        equipmentCategoryRepository.saveAll(categories);
    }
}