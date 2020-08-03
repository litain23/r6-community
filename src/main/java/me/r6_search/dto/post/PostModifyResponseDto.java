package me.r6_search.dto.post;

import lombok.Data;

@Data
public class PostModifyResponseDto {
    String message;
    long postId;

    public PostModifyResponseDto(String message, long postId) {
        this.message = message;
        this.postId = postId;
    }
}
