package com.tjlog.demo.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * status -> 400에러
 */
@Getter
@Setter
public class InvalidRequest extends TjlogException {
    private static final String MESSAGE = "존재하지 않는 글입니다";

    public InvalidRequest(){
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName,String message){
        super(MESSAGE);
        addValidation(fieldName,message);
    }


    @Override
    public int getStatusCode(){
        return 400;
    }
}
