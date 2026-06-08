package com.Carelio.household_service.config;

import com.Carelio.household_service.entity.Room;
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

    @Override
    public void run(String... args) {
        if (roomRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        List<Room> rooms = List.of(
                Room.builder()
                        .ownerId(1L)
                        .name("Phòng khách")
                        .floor(1)
                        .description("Phòng chính để tiếp khách và sinh hoạt chung")
                        .deleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),

                Room.builder()
                        .ownerId(1L)
                        .name("Phòng ngủ chính")
                        .floor(2)
                        .description("Phòng ngủ của chủ nhà")
                        .deleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),

                Room.builder()
                        .ownerId(1L)
                        .name("Nhà bếp")
                        .floor(1)
                        .description("Khu vực nấu ăn và ăn uống")
                        .deleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),

                Room.builder()
                        .ownerId(1L)
                        .name("Phòng làm việc")
                        .floor(2)
                        .description("Phòng dùng để học tập và làm việc")
                        .deleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),

                Room.builder()
                        .ownerId(1L)
                        .name("Phòng tắm")
                        .floor(1)
                        .description("Phòng tắm tầng 1")
                        .deleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .build(),

                Room.builder()
                        .ownerId(2L)
                        .name("Phòng khách")
                        .floor(1)
                        .description("Phòng khách của user khác để test lọc theo ownerId")
                        .deleted(false)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()
        );

        roomRepository.saveAll(rooms);
    }
}