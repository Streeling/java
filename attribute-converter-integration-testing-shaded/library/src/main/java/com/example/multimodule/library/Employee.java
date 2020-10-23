package com.example.multimodule.library;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = BooleanToIntegerConverter.class)
    private boolean fullTime;
}