package com.Carelio.household_service.config;

import com.Carelio.household_service.entity.House;
import com.Carelio.household_service.entity.Room;
import com.Carelio.household_service.repository.HouseRepository;
import com.Carelio.household_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Order(2)
public class RoomDataSeeder implements CommandLineRunner {

    private final RoomRepository roomRepository;
    private final HouseRepository houseRepository;

    @Override
    public void run(String... args) {
        if (roomRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        House houseUser1 = houseRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("House id 1 not found"));
        House houseUser2 = houseRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("House id 2 not found"));

        List<Room> rooms = List.of(
                Room.builder()
                        .name("Phòng khách")
                        .floor(1)
                        .description("Phòng chính để tiếp khách và sinh hoạt chung")
                        .isDeleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .house(houseUser1)
                        .build(),

                Room.builder()
                        .name("Phòng ngủ chính")
                        .floor(2)
                        .description("Phòng ngủ của chủ nhà")
                        .isDeleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .house(houseUser1)
                        .build(),

                Room.builder()
                        .name("Nhà bếp")
                        .floor(1)
                        .description("Khu vực nấu ăn và ăn uống")
                        .isDeleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .house(houseUser1)
                        .build(),

                Room.builder()
                        .name("Phòng làm việc")
                        .floor(2)
                        .description("Phòng dùng để học tập và làm việc")
                        .isDeleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .house(houseUser1)
                        .build(),

                Room.builder()
                        .name("Phòng tắm")
                        .floor(1)
                        .description("Phòng tắm tầng 1")
                        .isDeleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .house(houseUser1)
                        .build(),

                Room.builder()
                        .name("Phòng khách")
                        .floor(1)
                        .description("Phòng khách của user khác để test lọc theo ownerId")
                        .isDeleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .house(houseUser2)
                        .build()
        );

        roomRepository.saveAll(rooms);
    }
}