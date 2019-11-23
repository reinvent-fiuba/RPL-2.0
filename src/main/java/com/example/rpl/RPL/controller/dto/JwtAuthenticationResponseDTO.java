package com.example.rpl.RPL.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by rajeevkumarsingh on 19/08/17.
 */
@AllArgsConstructor
@Getter
public class JwtAuthenticationResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer";
}
