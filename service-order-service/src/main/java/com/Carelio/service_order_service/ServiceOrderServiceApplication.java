package com.Carelio.service_order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class ServiceOrderServiceApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(ServiceOrderServiceApplication.class, args);
	}

}
