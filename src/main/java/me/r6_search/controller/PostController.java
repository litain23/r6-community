package me.r6_search.controller;

import lombok.RequiredArgsConstructor;
import me.r6_search.config.UserProfileAnnotation;
import me.r6_search.exception.board.PostIllegalFileExtensionException;
import me.r6_search.model.userprofile.UserProfile;
import me.r6_search.service.PostService;
import me.r6_search.dto.post.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

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

    @PostMapping(value = "/post", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity makePost(PostSaveRequestDto requestDto,
                                   @UserProfileAnnotation UserProfile userProfile) {
        checkFilesExtension(requestDto.getFiles());
        postService.savePost(requestDto, userProfile);
        return ResponseEntity.ok("hello");
    }

    private void checkFilesExtension(MultipartFile[] files) {
        if(files == null) return;
        for(MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            if(extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png") || extension.equals("gif")) continue;
            else throw new PostIllegalFileExtensionException("파일 확장자는 jpeg, jpg, png, gif 만 가능합니다");
        }
    }

    @PutMapping("/post/{id}")
    public long modifyPost(@PathVariable long id,
                           @RequestBody PostUpdateRequestDto requestDto,
                           @UserProfileAnnotation UserProfile userProfile) {
        return postService.modifyPost(id, requestDto, userProfile);
    }

    @DeleteMapping("/post/{id}")
    public long deletePost(@PathVariable long id,
                           @UserProfileAnnotation UserProfile userProfile) {
        return postService.deletePost(id, userProfile);
    }

    @PostMapping("/post/{id}/recommend/")
    public long toggleRecommendPost(@PathVariable long id,
                              @UserProfileAnnotation UserProfile userProfile) {
        return postService.toggleRecommendPost(id, userProfile);
    }
}
