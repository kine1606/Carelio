package com.amigoscode.carelio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CarelioApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(CarelioApplication.class, args);
    }

}
