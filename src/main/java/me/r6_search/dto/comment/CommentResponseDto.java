package me.r6_search.dto.comment;

import lombok.Data;
import me.r6_search.model.comment.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentResponseDto {
    long commentId;
    long parentId;
    String parentUsername;
    String username;
    String content;
    List<CommentResponseDto> childComment = new ArrayList<>();
    LocalDateTime createdTime;
    boolean isChild;

    public static CommentResponseDto of(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setCommentId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedTime(comment.getCreatedTime());
        dto.setUsername(comment.getUserProfile().getUsername());
        return dto;
    }
}
