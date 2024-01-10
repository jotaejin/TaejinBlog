package com.tjlog.demo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 *  {
 *      "code":"400",
 *      "message":"잘못된 요청,
 *  }
 */
@Getter
//비어 있지 않은 데이터만 json으로 응답
/*@JsonInclude(value = JsonInclude.Include.NON_EMPTY)*/
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String,String> validation;

    public void addValidation(String fieldName, String errorMessage){
      this.validation.put(fieldName,errorMessage);
    }

    @Builder
    public ErrorResponse(String code, String message,Map<String,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }
}
