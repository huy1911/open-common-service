package com.lpb.mid.exception;


import com.lpb.mid.dto.ResponseDto;
import com.lpb.mid.dto.ResponseObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
@Log4j2
public class ResExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ExceptionHandler.class)
    public ResponseEntity<?> handleTransferException(ExceptionHandler exceptionHandler) {
        ResponseDto<?> response = exceptionHandler.getResponseDto();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("MethodArgumentNotValidException message = {}", exception.getMessage(), exception);
        ResponseDto<?> response = ResponseDto.builder()
                .type(ErrorMessage.ERR_20.type)
                .description(ErrorMessage.ERR_20.message + exception.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .statusCode(ErrorMessage.ERR_20.code)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
