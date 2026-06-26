package com.Carelio.service_order_service.client;

import com.Carelio.service_order_service.client.dto.WorkerProfileResponse;
import com.Carelio.service_order_service.client.dto.ServiceSkillResponse;
import com.Carelio.service_order_service.client.dto.WorkerSkillResponse;
import com.Carelio.service_order_service.config.FeignClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@FeignClient(
        name = "worker-service",
        contextId = "orderServiceWorkerClient",
        url = "${worker.service.url}",
        fallbackFactory = WorkerClient.WorkerClientFallbackFactory.class,
        configuration = FeignClientConfig.class
)
public interface WorkerClient {

    @GetMapping("/api/workers/{workerId}")
    WorkerProfileResponse getWorkerProfile(@PathVariable("workerId") Long workerId);

    @GetMapping("/api/service-skills/{ssid}")
    ServiceSkillResponse getServiceSkill(@PathVariable("ssid") Long ssid);

    @GetMapping("/api/workers/{id}/skills")
    List<WorkerSkillResponse> getWorkerSkills(@PathVariable("id") Long id);

    @PatchMapping("/api/internal/workers/{workerId}/rating")
    WorkerProfileResponse updateRating(@PathVariable("workerId") Long workerId, @RequestParam("rating") Integer rating);

    @GetMapping("/api/workers/{workerId}/profile")
    WorkerProfileResponse getWorkerByKeycloakId(@PathVariable("workerId") String workerId);

    @Component
    @Slf4j
    class WorkerClientFallbackFactory implements org.springframework.cloud.openfeign.FallbackFactory<WorkerClient> {
        @Override
        public WorkerClient create(Throwable cause) {
            return new WorkerClient() {

                @Override
                public WorkerProfileResponse getWorkerProfile(Long workerId) {
                    log.error("Circuit Breaker [getWorkerProfile] activated! Error: {}", cause.getMessage());
                    WorkerProfileResponse fallback = new WorkerProfileResponse();
                    fallback.setId(workerId);
                    fallback.setBio("Temporary data");
                    fallback.setStatus(null);
                    return fallback;
                }

                @Override
                public ServiceSkillResponse getServiceSkill(Long ssid) {
                    log.error(" Circuit Breaker [getServiceSkill] activated! Error: {}", cause.getMessage());
                    ServiceSkillResponse fallback = new ServiceSkillResponse();
                    fallback.setId(ssid);
                    return fallback;
                }

                @Override
                public List<WorkerSkillResponse> getWorkerSkills(Long id) {
                    log.error(" Circuit Breaker [getWorkerSkills] activated! Error: {}", cause.getMessage());
                    return Collections.emptyList();
                }

                @Override
                public WorkerProfileResponse updateRating(Long workerId, Integer rating) {
                    log.error(" Circuit Breaker [updateRating] activated! Error: {}", cause.getMessage());
                    WorkerProfileResponse fallback = new WorkerProfileResponse();
                    fallback.setId(workerId);
                    return fallback;
                }

                @Override
                public WorkerProfileResponse getWorkerByKeycloakId(String workerId)
                {
                    log.error(" Circuit Breaker [getWorkerByKeycloakId] activated! Error: {}", cause.getMessage());
                    WorkerProfileResponse fallback = new WorkerProfileResponse();
                    fallback.setBio(workerId);
                    return fallback;
                }
            };
        }
    }
}