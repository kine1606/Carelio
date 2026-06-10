package com.Carelio.worker_service.client;

import com.Carelio.worker_service.client.dto.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "household-client", url = "${household.service.url}")
public interface HouseholdClient
{
    @GetMapping("/api/equipment-category/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id);
}
