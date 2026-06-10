package com.Carelio.worker_service.config.seeder;

import com.Carelio.worker_service.entity.ServiceSkill;
import com.Carelio.worker_service.entity.ServiceSkillCode;
import com.Carelio.worker_service.entity.SkillLevel;
import com.Carelio.worker_service.entity.WorkerProfile;
import com.Carelio.worker_service.entity.WorkerSkill;
import com.Carelio.worker_service.repository.ServiceSkillRepository;
import com.Carelio.worker_service.repository.WorkerProfileRepository;
import com.Carelio.worker_service.repository.WorkerSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Order(3)
public class WorkerSkillSeeder implements CommandLineRunner {

    private final WorkerSkillRepository workerSkillRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final ServiceSkillRepository serviceSkillRepository;

    @Override
    public void run(String... args) {
        if (workerSkillRepository.count() > 0) {
            return;
        }

        List<WorkerProfile> profiles = workerProfileRepository.findAll();
        if (profiles.isEmpty()) {
            return;
        }

        ServiceSkill repair = serviceSkillRepository.findByServiceSkillCodeIgnoreCase(ServiceSkillCode.REPAIR)
                .orElse(null);
        ServiceSkill cleaning = serviceSkillRepository.findByServiceSkillCodeIgnoreCase(ServiceSkillCode.CLEANING)
                .orElse(null);
        ServiceSkill installation = serviceSkillRepository.findByServiceSkillCodeIgnoreCase(ServiceSkillCode.INSTALLATION)
                .orElse(null);

        List<WorkerSkill> seeds = new ArrayList<>();

        // Assign some sample skills to first few workers if available
        if (profiles.size() >= 1 && repair != null) {
            seeds.add(WorkerSkill.builder()
                    .workerProfile(profiles.get(0))
                    .serviceSkill(repair)
                    .equipmentCategoryId(1L)
                    .yearExperience(3)
                    .skillLevel(SkillLevel.ADVANCED)
                    .build());
        }
        if (profiles.size() >= 2 && cleaning != null) {
            seeds.add(WorkerSkill.builder()
                    .workerProfile(profiles.get(1))
                    .serviceSkill(cleaning)
                    .equipmentCategoryId(2L)
                    .yearExperience(2)
                    .skillLevel(SkillLevel.INTERMEDIATE)
                    .build());
        }
        if (profiles.size() >= 3 && installation != null) {
            seeds.add(WorkerSkill.builder()
                    .workerProfile(profiles.get(2))
                    .serviceSkill(installation)
                    .equipmentCategoryId(3L)
                    .yearExperience(4)
                    .skillLevel(SkillLevel.BEGINNER)
                    .build());
        }

        if (!seeds.isEmpty()) {
            workerSkillRepository.saveAll(seeds);
        }
    }
}
