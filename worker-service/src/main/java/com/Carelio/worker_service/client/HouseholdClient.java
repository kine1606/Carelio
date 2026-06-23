package com.Carelio.worker_service.client;

import com.Carelio.worker_service.client.dto.CategoryResponse;
import com.Carelio.worker_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "household-client",
        contextId = "workerServiceHouseholdClient",
        url = "${household.service.url}",
        configuration = FeignClientConfig.class)
public interface HouseholdClient
{
    @GetMapping("/api/equipment-category/{id}")
    CategoryResponse getCategoryById(@PathVariable Long id);
}
