package me.r6_search.dto.post;

import lombok.Data;

@Data
public class PostRecommendResponseDto {
    String message;
    boolean isRecommend;

    public PostRecommendResponseDto(String message, boolean isRecommend) {
        this.message = message;
        this.isRecommend = isRecommend;
    }
}
