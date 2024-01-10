package com.tjlog.demo.controller;

import com.tjlog.demo.exception.InvalidRequest;
import com.tjlog.demo.exception.TjlogException;
import com.tjlog.demo.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

//모든 컨트롤러에서 발생할 수 있는 예외를 잡아 처리해주는 애노테이션
@RestControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)//이 Exception 에 대해서만 캐치를해서 json 으로 만듬
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e){

            //ErrorResponse 클래스를 만들어서 json으로 반환
            ErrorResponse errorResponse  = ErrorResponse.builder().
                    code("400")
                    .message("잘못된 요청입니다.")
                    .build();

        for (FieldError fieldError: e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return errorResponse;
    }

    @ExceptionHandler(TjlogException.class)
    public ResponseEntity<ErrorResponse> tjlogException(TjlogException e){
        int statusCode = e.getStatusCode();

        ErrorResponse body  = ErrorResponse.builder().
                code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        //응답 json validation -> title : "제목에 바보를 포함할 수 없습니다

        //응답코드와 바디를 제이슨으로 응답해준다
        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity.status(statusCode)
                .body(body);
        return responseEntity;
    }


}
