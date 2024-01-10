package com.tjlog.demo.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class TjlogException extends RuntimeException{

    private final Map<String,String> validation = new HashMap<>();

    public TjlogException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName,String message){
        validation.put(fieldName,message);
    }
}
