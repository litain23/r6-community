package me.r6_search.dto;

import lombok.Getter;

@Getter
public class JwtResponseDto {
    String jwtToken;
    String refreshToken;


    public JwtResponseDto(String jwtToken, String refreshToken) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }
}
