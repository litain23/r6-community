package me.r6_search.controller;

import lombok.RequiredArgsConstructor;
import me.r6_search.dto.comment.CommentModifyResponseDto;
import me.r6_search.dto.comment.CommentSaveRequestDto;
import me.r6_search.dto.comment.CommentUpdateRequestDto;
import me.r6_search.exception.user.UserAuthenticationException;
import me.r6_search.service.CommentService;
import me.r6_search.config.UserProfileAnnotation;
import me.r6_search.model.userprofile.UserProfile;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/c")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public CommentModifyResponseDto makeComment(@RequestBody CommentSaveRequestDto requestDto,
                                                @UserProfileAnnotation UserProfile userProfile) {
        long commentId = commentService.saveComment(requestDto, userProfile);
        return new CommentModifyResponseDto("댓글이 작성되었습니다", commentId);
    }


    @PutMapping("/comment/{commentId}")
    public CommentModifyResponseDto modifyComment(@PathVariable long commentId,
                              @RequestBody CommentUpdateRequestDto requestDto,
                              @UserProfileAnnotation UserProfile userProfile) {
        commentService.modifyComment(commentId, requestDto, userProfile);
        return new CommentModifyResponseDto("댓글이 수정되었습니다.", commentId);

    }

    @DeleteMapping("/comment/{commentId}")
    public CommentModifyResponseDto deleteComment(@PathVariable long commentId,
                              @UserProfileAnnotation UserProfile userProfile) {
        commentService.deleteComment(commentId, userProfile);
        return new CommentModifyResponseDto("댓글이 삭제되었습니다.", commentId);
    }

}
