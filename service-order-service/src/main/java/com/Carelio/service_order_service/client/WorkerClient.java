package com.Carelio.service_order_service.client;

import com.Carelio.service_order_service.client.dto.WorkerProfileResponse;
import com.Carelio.service_order_service.client.dto.ServiceSkillResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="worker-service", url = "${worker.service.url}")
public interface WorkerClient
{
    @GetMapping("/api/workers/{workerId}")
    public WorkerProfileResponse getWorkerProfile(@PathVariable("workerId") Long workerId);

    @GetMapping("/api/service-skills/{ssid}")
    public ServiceSkillResponse getServiceSkill(@PathVariable("ssid") Long ssid);

}
