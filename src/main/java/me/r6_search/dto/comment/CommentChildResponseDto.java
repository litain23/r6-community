package me.r6_search.dto.comment;

import lombok.Builder;
import lombok.Data;
import me.r6_search.model.comment.Comment;

import java.time.LocalDateTime;

@Data
public class CommentChildResponseDto {
    long commentId;
    long parentId;
    String replayUsername;
    String username;
    String content;
    LocalDateTime createdTime;

    public static CommentChildResponseDto of(Comment comment) {
        CommentChildResponseDto responseDto = new CommentChildResponseDto();
        responseDto.setCommentId(comment.getId());
        responseDto.setParentId(comment.getParentComment().getId());
        responseDto.setContent(comment.getContent());
        responseDto.setReplayUsername(comment.getReplayUsername());
        responseDto.setUsername(comment.getUserProfile().getUsername());
        responseDto.setCreatedTime(comment.getCreatedTime());
        return responseDto;
    }
}
