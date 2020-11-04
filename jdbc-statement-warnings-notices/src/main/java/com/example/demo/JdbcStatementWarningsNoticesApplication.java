package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JdbcStatementWarningsNoticesApplication {

	public static void main(String[] args) {
		SpringApplication.run(JdbcStatementWarningsNoticesApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(PersonRepository personRepository) {
		return args -> {
			personRepository.longRunningFunction(1);
		};
	}
}