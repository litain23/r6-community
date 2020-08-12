package me.r6_search.controller;

import lombok.RequiredArgsConstructor;
import me.r6_search.config.UserProfileAnnotation;
import me.r6_search.exception.board.PostIllegalFileExtensionException;
import me.r6_search.model.userprofile.UserProfile;
import me.r6_search.service.PostService;
import me.r6_search.dto.post.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/c")
@RestController
public class PostController {
    private final PostService postService;

    @GetMapping("/topic/{type}")
    public TopicSummaryDto getPostList(@PathVariable String type,
                                        @RequestParam(defaultValue = "1", required = false) int page) {
        return postService.getCategoryPostList(type, page);
    }

    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable long id,
                                   @UserProfileAnnotation UserProfile userProfile) {
        return postService.getPost(id, userProfile);
    }

    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostModifyResponseDto makePost(PostSaveRequestDto requestDto,
                                          @UserProfileAnnotation UserProfile userProfile) {
        Long postId = postService.savePost(requestDto, userProfile);
        return new PostModifyResponseDto("게시글이 생성되었습니다", postId);
    }

    @PutMapping("/post/{id}")
    public PostModifyResponseDto modifyPost(@PathVariable long id,
                                            @RequestBody PostUpdateRequestDto requestDto,
                                            @UserProfileAnnotation UserProfile userProfile) {
        Long postId = postService.modifyPost(id, requestDto, userProfile);
        return new PostModifyResponseDto("게시글이 수정되었습니다", postId);
    }

    @DeleteMapping("/post/{id}")
    public PostModifyResponseDto deletePost(@PathVariable long id,
                                            @UserProfileAnnotation UserProfile userProfile) {
        Long postId = postService.deletePost(id, userProfile);
        return new PostModifyResponseDto("게시글이 삭제되었습니다", postId);
    }

    @PostMapping("/post/{id}/recommend/")
    public PostRecommendResponseDto toggleRecommendPost(@PathVariable long id,
                              @UserProfileAnnotation UserProfile userProfile) {
        boolean isRecommend = postService.toggleRecommendPost(id, userProfile);
        if(isRecommend) {
            return new PostRecommendResponseDto("이 게시글을 추천을 하였습니다.");
        } else {
            return new PostRecommendResponseDto("이 게시글 추천을 취소하였습니다.");
        }
    }
}
