package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void longRunningWork(int taskCount) {
        personRepository.longRunningFunction(taskCount);
    }
}
