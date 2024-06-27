package com.lpb.mid.exception;

import com.lpb.mid.dto.ResponseDto;
import lombok.Data;

@Data
public class ExceptionHandler extends RuntimeException {

    private ResponseDto<?> responseDto;


    public ExceptionHandler(ErrorMessage errorMessage) {
        this.responseDto = ResponseDto.builder()
                .statusCode(errorMessage.getCode())
                .description(errorMessage.getMessage())
                .type(errorMessage.getType())
                .build();
    }
}
