package me.r6_search.dto.post;

import lombok.Data;

@Data
public class PostSaveResponseDto {
    String response;

    public PostSaveResponseDto(String response) {
        this.response = response;
    }
}
