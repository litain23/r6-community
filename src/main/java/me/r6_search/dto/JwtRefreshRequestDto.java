package me.r6_search.dto;

import lombok.Data;

@Data
public class JwtRefreshRequestDto {
    String refreshToken;

    public JwtRefreshRequestDto() { }

    public JwtRefreshRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
