package com.Carelio.worker_service.config.seeder;

import com.Carelio.worker_service.entity.ServiceSkill;
import com.Carelio.worker_service.entity.ServiceSkillCode;
import com.Carelio.worker_service.repository.ServiceSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Order(1)
public class ServiceSkillSeeder implements CommandLineRunner
{

    private final ServiceSkillRepository serviceSkillRepository;

    @Override
    public void run(String... args)
    {
        if (serviceSkillRepository.count() > 0) {
            return;
        }

        List<ServiceSkill> skills = List.of(
                ServiceSkill.builder()
                        .serviceSkillCode(ServiceSkillCode.REPAIR)
                        .build(),
                ServiceSkill.builder()
                        .serviceSkillCode(ServiceSkillCode.CLEANING)
                        .build(),
                ServiceSkill.builder()
                        .serviceSkillCode(ServiceSkillCode.MAINTENANCE)
                        .build(),
                ServiceSkill.builder()
                        .serviceSkillCode(ServiceSkillCode.INSTALLATION)
                        .build(),
                ServiceSkill.builder()
                        .serviceSkillCode(ServiceSkillCode.PART_REPLACEMENT)
                        .build()
        );

        serviceSkillRepository.saveAll(skills);
    }
}