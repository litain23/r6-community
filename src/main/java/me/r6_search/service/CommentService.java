package me.r6_search.service;

import lombok.RequiredArgsConstructor;
import me.r6_search.dto.comment.CommentChildResponseDto;
import me.r6_search.model.comment.Comment;
import me.r6_search.model.comment.CommentRepository;
import me.r6_search.model.post.Post;
import me.r6_search.dto.comment.CommentResponseDto;
import me.r6_search.dto.comment.CommentSaveRequestDto;
import me.r6_search.dto.comment.CommentUpdateRequestDto;
import me.r6_search.exception.board.PostNotFoundException;
import me.r6_search.exception.board.CommentIllegalModifyException;
import me.r6_search.exception.board.CommentNotFoundException;
import me.r6_search.model.post.PostRepository;
import me.r6_search.model.userprofile.UserProfile;
import me.r6_search.model.userprofile.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserProfileService userProfileService;

    public List<CommentResponseDto> getCommentListAtPost(long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        if(commentList == null) return Collections.EMPTY_LIST;

        Map<Comment, List<Comment>> childCommentMap = commentList.stream()
                .filter(comment -> comment.getParentComment() != null)
                .collect(Collectors.groupingBy(comment -> comment.getParentComment()));

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList) {
            // 대댓인 경우는 parent comment 가 처리될때, 하위로 들어감
            if(comment.getParentComment() != null) continue;
            CommentResponseDto responseDto = CommentResponseDto.of(comment);

            List<Comment> childCommentList = childCommentMap.get(comment);
            if(childCommentList != null) {
                childCommentList.sort(Comparator.comparing(Comment::getCreatedTime));
                for(Comment childComment : childCommentList) {
                    responseDto.getChildComment().add(CommentChildResponseDto.of(childComment));
                }
            }
            commentResponseDtoList.add(responseDto);
        }
        return commentResponseDtoList;
    }

    @Transactional
    public long saveComment(CommentSaveRequestDto requestDto, UserProfile userProfile) {
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        Comment comment = requestDto.toEntity(post, userProfile);

        long commentId = commentRepository.save(comment).getId();
        if(requestDto.getParentCommentId() != 0) {
            Comment parentComment = commentRepository.findById(requestDto.getParentCommentId()).orElseThrow(() -> new CommentNotFoundException("댓글이 존재하지 않습니다."));
            userProfileService.getUserProfile(requestDto.getReplayUsername());
            parentComment.addChildComment(comment);
            comment.setParentComment(parentComment);
        }
        return commentId;
    }

    @Transactional
    public long modifyComment(long commentId, CommentUpdateRequestDto requestDto, UserProfile userProfile) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("수정할 댓글이 존재하지 않습니다."));
        if(comment.getUserProfile().getId() != userProfile.getId()) {
            throw new CommentIllegalModifyException("댓글을 수정할 권한이 없습니다.");
        }

        comment.updateContent(requestDto.getContent());
        return comment.getId();
    }

    @Transactional
    public long deleteComment(long commentId, UserProfile userProfile) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("수정할 댓글이 존재하지 않습니다."));
        if(comment.getUserProfile().getId() != userProfile.getId()) {
            throw new CommentIllegalModifyException("댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
        return commentId;
    }

    protected int getCommentCnt(long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        if(commentList == null) return 0;
        else return commentList.size();
    }
}
