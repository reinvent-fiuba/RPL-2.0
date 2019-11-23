package com.example.rpl.RPL.controller.dto;

import lombok.Value;

@Value
public class JwtResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
}
