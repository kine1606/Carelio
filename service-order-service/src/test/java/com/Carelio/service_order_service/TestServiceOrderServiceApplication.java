package com.Carelio.service_order_service;

import org.springframework.boot.SpringApplication;

public class TestServiceOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ServiceOrderServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
