package com.Carelio.worker_service.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.Carelio.worker_service.client")
public class FeignConfig {
}
