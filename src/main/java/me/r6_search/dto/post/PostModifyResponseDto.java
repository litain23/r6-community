package me.r6_search.dto.post;

import lombok.Data;

@Data
public class PostModifyResponseDto {
    long postId;
    String message;

    public PostModifyResponseDto(String message, long postId) {
        this.message = message;
        this.postId = postId;
    }
}
