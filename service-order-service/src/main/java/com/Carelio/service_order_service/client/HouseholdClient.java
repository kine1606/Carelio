package com.Carelio.service_order_service.client.dto;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="household-service", url = "${household.service.url}")
public class HouseholdClient
{
    
}
