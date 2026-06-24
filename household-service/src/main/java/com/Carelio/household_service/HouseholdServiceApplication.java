package com.Carelio.household_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HouseholdServiceApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(HouseholdServiceApplication.class, args);
	}

}
