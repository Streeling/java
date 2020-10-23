package com.example.multimodule.library;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToIntegerConverter implements AttributeConverter<Boolean, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Boolean aBoolean) {
        return aBoolean != null && aBoolean ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer anInteger) {
        return anInteger != null && anInteger == 1 ? true : false;
    }
}