package com.example.multimodule.application;

import com.example.multimodule.library.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@DataJpaTest
public class SomeIntegrationTest {

    @Autowired
    private EmployeeFacade employeeFacade;

    @Test
    public void test() {
        employeeFacade.abc();
    }

    @Configuration
    @Import(DemoApplication.class)
    protected static class TestConfiguration {

        @Bean
        public EmployeeFacade employeeFacade(EmployeeRepository employeeRepository) {
            return new EmployeeFacade(employeeRepository);
        }
    }
}