package com.lpb.mid.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ResT24Dto<T> {
    private String status;
    private String error;
    private String path;
    @JsonProperty("clientMessageId")
    private String clientMessageId;
    private T data;
}
