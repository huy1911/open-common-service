package com.lpb.mid.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private String expiration;

    public JwtResponse(String accessToken, String expiration) {
        this.token = accessToken;
        this.expiration = expiration;
    }

}