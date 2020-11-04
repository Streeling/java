package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {

    @Query(nativeQuery = true, value = "SELECT long_running(:n)")
    boolean longRunningFunction(int n);
}
