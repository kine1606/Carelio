package com.Carelio.household_service;

import org.springframework.boot.SpringApplication;

public class TestHouseholdServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(HouseholdServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
