package com.example.multimodule.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = "com.example.multimodule")
@EntityScan("com.example.multimodule.library")
@EnableJpaRepositories(basePackages = {
		"com.example.multimodule.library"
})
@RestController
public class DemoApplication {

//	private final MyService myService;
//
//	public DemoApplication(MyService myService) {
//		this.myService = myService;
//	}
//
	@Autowired
	private EmployeeFacade employeeFacade;

	@GetMapping("/")
	public String home() {
//		return myService.message();
		return "Multi module application";
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
