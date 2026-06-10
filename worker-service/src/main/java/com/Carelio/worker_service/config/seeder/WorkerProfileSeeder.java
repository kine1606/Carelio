package com.Carelio.worker_service.config.seeder;

import com.Carelio.worker_service.entity.WorkerProfile;
import com.Carelio.worker_service.repository.WorkerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Order(2)
public class WorkerProfileSeeder implements CommandLineRunner {

    private final WorkerProfileRepository workerProfileRepository;

    @Override
    public void run(String... args) {
        if (workerProfileRepository.count() > 0) {
            return;
        }

        List<WorkerProfile> profiles = List.of(
                createProfile(101L, "Experienced technician specializing in repairs."),
                createProfile(102L, "Clean and detail specialist."),
                createProfile(103L, "Installer with focus on home appliances.")
        );

        workerProfileRepository.saveAll(profiles);
    }

    private WorkerProfile createProfile(Long userId, String bio) {
        WorkerProfile p = new WorkerProfile();
        p.setUserId(userId);
        p.setBio(bio);
        // other fields (e.g., totalJobs/rating/status) can rely on defaults/pre-persist if present
        return p;
    }
}
