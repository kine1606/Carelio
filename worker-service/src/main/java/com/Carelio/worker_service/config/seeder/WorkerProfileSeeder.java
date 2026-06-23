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

    String keycloakUser1 = "51c6c640-fe50-4e87-b72f-a49396f1c830";
    String keycloakUser2 = "c83a7b12-91d4-4f62-bc8a-123456789abc";
    @Override
    public void run(String... args) {
        if (workerProfileRepository.count() > 0) {
            return;
        }

        List<WorkerProfile> profiles = List.of(
                createProfile(keycloakUser1, "Experienced technician specializing in repairs."),
                createProfile(keycloakUser2, "Clean and detail specialist.")
        );

        workerProfileRepository.saveAll(profiles);
    }

    private WorkerProfile createProfile(String userId, String bio) {
        WorkerProfile p = new WorkerProfile();
        p.setUserId(userId);
        p.setBio(bio);
        // other fields (e.g., totalJobs/rating/status) can rely on defaults/pre-persist if present
        return p;
    }
}
