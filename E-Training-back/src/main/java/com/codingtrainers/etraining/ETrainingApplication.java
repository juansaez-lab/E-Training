package com.codingtrainers.etraining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ETrainingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ETrainingApplication.class, args);
	}

}
