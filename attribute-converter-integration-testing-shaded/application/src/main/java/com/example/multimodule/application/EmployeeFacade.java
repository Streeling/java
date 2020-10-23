package com.example.multimodule.application;

import com.example.multimodule.library.EmployeeRepository;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFacade {
    private EmployeeRepository employeeRepository;

    public EmployeeFacade(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void abc() {
        employeeRepository.findAll();
    }
}
