package com.Carelio.service_order_service.client;

import com.Carelio.service_order_service.client.dto.ServiceSkillResponse;
import com.Carelio.service_order_service.client.dto.WorkerProfileResponse;
import com.Carelio.service_order_service.client.dto.WorkerSkillResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="worker-service",contextId = "orderServiceWorkerClient", url = "${worker.service.url}")
public interface WorkerClient
{
    @GetMapping("/api/workers/{workerId}")
    public WorkerProfileResponse getWorkerProfile(@PathVariable("workerId") Long workerId);

    @GetMapping("/api/service-skills/{ssid}")
    public ServiceSkillResponse getServiceSkill(@PathVariable("ssid") Long ssid);

    @GetMapping("/api/workers/{id}/skills")
    public List<WorkerSkillResponse> getWorkerSkills(@PathVariable("id") Long id);
    @PatchMapping("/api/workers/{workerId}")
    public WorkerProfileResponse updateRating(@PathVariable("workerId") Long workerId, Integer rating);


}
