package com.lpb.mid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ResT24Error {
    private String status;
    private String error;
    private String soaErrorCode;
    private String soaErrorDesc;
    private String clientMessageId;
    private String path;
}
