package com.lpb.mid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto<T> {
    private T data;
    private String statusCode;
    private String description;
    private String type;
}
