package com.Carelio.worker_service;

import org.springframework.boot.SpringApplication;

public class TestWorkerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(WorkerServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
