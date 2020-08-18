package me.r6_search.dto.post;

import lombok.Data;

@Data
public class PostRecommendResponseDto {
    String message;

    public PostRecommendResponseDto(String message) {
        this.message = message;
    }
}
