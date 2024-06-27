package com.lpb.mid.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@With
@Builder
public class ResponseObject {
    private Integer status;
    private String message;
    private static final String SUCCESS = "Successfully";
    private static final String FAILED = "Failed";
    private static final String NOT_FOUND = "Not Found";

    public ResponseObject(HttpStatus status, String message, Object data) {
        this.status = status.value();
        this.message = message;
    }

    public ResponseObject(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public ResponseObject(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResponseEntity<JwtResponse> successLogin(JwtResponse data) {
        return ResponseEntity.ok().body(data);
    }

    public static ResponseEntity<ResponseObject> success(Object data) {
        return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, SUCCESS, data));
    }

    public static ResponseEntity<ResponseObject> unauthorized(String message) {
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.UNAUTHORIZED, message));
    }

    public static ResponseEntity<ResponseObject> success(Object data, String message) {
        return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, message, data));
    }

    public static ResponseEntity<ResponseObject> createSuccess(Object data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(HttpStatus.CREATED, SUCCESS, data));
    }


    public static ResponseEntity<ResponseObject> badRequest(String message) {
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.BAD_REQUEST, message));
    }

    public static ResponseEntity<ResponseObject> notFound() {
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.NOT_FOUND, NOT_FOUND));
    }

    public static ResponseEntity<ResponseObject> bad(String message) {
        return ResponseEntity.badRequest().body(new ResponseObject(HttpStatus.NOT_FOUND, message));
    }

    public static ResponseEntity<?> build(HttpStatus status, String message) {
        return ResponseEntity.badRequest().body(new ResponseObject(status, message));
    }
}
