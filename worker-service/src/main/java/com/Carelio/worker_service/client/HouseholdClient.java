package com.Carelio.worker_service.client;

import com.Carelio.worker_service.client.dto.CategoryResponse;
import com.Carelio.worker_service.config.FeignClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "household-client",
        contextId = "workerServiceHouseholdClient",
        url = "${household.service.url}",
        fallbackFactory = HouseholdClient.HouseholdClientFallbackFactory.class,
        configuration = FeignClientConfig.class)
public interface HouseholdClient
{
    @GetMapping("/api/equipment-category/{id}")
    CategoryResponse getCategoryById(@PathVariable Long id);

    @Component
    @Slf4j
    class HouseholdClientFallbackFactory implements FallbackFactory<HouseholdClient>
    {
        @Override
        public HouseholdClient create(Throwable cause)
        {
            return new HouseholdClient()
            {
                @Override
                public CategoryResponse getCategoryById(Long ecid)
                {
                    log.error("Circuit Breaker [getEquipmentCategoryById]activated! Error: {}", cause.getMessage());
                    CategoryResponse fallback = new CategoryResponse();
                    fallback.setId(ecid);
                    fallback.setName("Temporary data");
                    return fallback;
                }
            };
        }
    }
}
