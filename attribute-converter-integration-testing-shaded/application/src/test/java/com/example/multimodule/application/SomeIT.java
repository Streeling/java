package com.example.multimodule.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class SomeIT {

    @Autowired
    private EmployeeFacade employeeFacade;

    @Test
    public void test() {
        employeeFacade.abc();
    }
}