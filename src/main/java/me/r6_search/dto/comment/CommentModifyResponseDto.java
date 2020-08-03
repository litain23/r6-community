package me.r6_search.dto.comment;

import lombok.Data;

@Data
public class CommentModifyResponseDto {
    String message;
    long id;

    public CommentModifyResponseDto(String message, long id) {
        this.message = message;
        this.id = id;
    }
}
