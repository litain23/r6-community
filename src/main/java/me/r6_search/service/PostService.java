package me.r6_search.service;

import lombok.RequiredArgsConstructor;
import me.r6_search.model.imgsrc.ImgSrc;
import me.r6_search.model.imgsrc.ImgSrcRepository;
import me.r6_search.model.post.Post;
import me.r6_search.model.post.PostRepository;
import me.r6_search.model.post.PostType;
import me.r6_search.model.postrecommend.PostRecommend;
import me.r6_search.model.postrecommend.PostRecommendRepository;
import me.r6_search.model.userprofile.UserProfile;
import me.r6_search.exception.board.BoardException;
import me.r6_search.exception.board.PostIllegalModifyException;
import me.r6_search.exception.board.PostNotFoundException;
import me.r6_search.dto.post.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRecommendRepository postRecommendRepository;
    private final PostRepository postRepository;
    private final CommentService commentService;
    private final ImgSrcService imgSrcService;

    private final int VIEW_POST_CNT = 20;

    @Transactional(readOnly = true)
    public TopicSummaryDto getCategoryPostList(String type, int page) {
        long postCnt = postRepository.countByType(PostType.valueOf(type));
        // 페이지가 수가 잘못되었을 때는 그냥 page 를 1로 하고 진행
        if(page <= 0 || postCnt / 20 + 1 < page) {
            page = 1;
        }

        PageMetaDto metaDto = PageMetaDto.builder()
                .currentPage(page)
                .totalPage(postCnt / 20 + 1)
                .build();

        // 공지사항 가져오기
        List<Post> postSummaryList = addNoticePost();

        Pageable pageable = PageRequest.of(page - 1, VIEW_POST_CNT, Sort.Direction.DESC, "createdTime");
        postSummaryList.addAll(postRepository.findByType(PostType.valueOf(type), pageable));

        List<PostSummaryDto> postSummaryDtoList = new ArrayList<>();
        for(Post post : postSummaryList) {
            PostSummaryDto dto = PostSummaryDto.builder()
                    .author(post.getUserProfile().getUsername())
                    .title(post.getTitle())
                    .hasImg(post.getImgSrcList().size() > 0)
                    .createdTime(post.getCreatedTime())
                    .recommendCnt(post.getRecommendCnt())
                    .viewCnt(post.getViewCnt())
                    .commentCnt(commentService.getCommentCnt(post.getId()))
                    .isNotice(post.isNotice())
                    .postId(post.getId())
                    .build();

            postSummaryDtoList.add(dto);
        }

        TopicSummaryDto summaryDto = TopicSummaryDto.builder()
                .meta(metaDto)
                .postList(postSummaryDtoList)
                .build();

        return summaryDto;
    }

    private List<Post> addNoticePost() {
        List<Post> noticePostList = postRepository.findByType(PostType.valueOf("notice"));
        if(noticePostList == null) return Collections.EMPTY_LIST;
        return noticePostList;
    }


    @Transactional
    public PostResponseDto getPost(long postId, UserProfile userProfile) {
        Post post = findPostById(postId);
        PostResponseDto responseDto = PostResponseDto.of(post);
        responseDto.setCommentList(commentService.getCommentListAtPost(postId));
        post.increaseViewCnt();

        PostRecommend isRecommend = postRecommendRepository.findByPostIdAndUserProfile(postId, userProfile);
        if(isRecommend != null) {
            responseDto.setRecommend(true);
        }
        return responseDto;
    }

    @Transactional
    public Long savePost(PostSaveRequestDto requestDto, UserProfile userProfile) throws RuntimeException{
        if(requestDto.getType() == "notice") throw new BoardException("권한이 없습니다");

        Post post = requestDto.toEntity(userProfile);
        postRepository.save(post);

        if(requestDto.getImgSrcList() != null) {
            imgSrcService.updateImgSrc(requestDto.getImgSrcList(), post);
        }
        return post.getId();
    }

    @Transactional
    public long modifyPost(long postId, PostUpdateRequestDto requestDto, UserProfile userProfile) {
        Post post = findPostById(postId);
        checkAuthenticationForModify(post, userProfile);
        post.updatePost(requestDto);
        return postId;
    }

    @Transactional
    public long deletePost(long postId, UserProfile userProfile) {
        Post deletePost = findPostById(postId);
        checkAuthenticationForModify(deletePost, userProfile);
        postRepository.delete(deletePost);
        return postId;
    }


    @Transactional
    public boolean toggleRecommendPost(long postId, UserProfile userProfile) {
        PostRecommend isRecommend = postRecommendRepository.findByPostIdAndUserProfile(postId, userProfile);
        if(isRecommend != null) {
            postRecommendRepository.delete(isRecommend);
            return false;
        } else {
            Post post = findPostById(postId);
            postRecommendRepository.save(
                PostRecommend.builder()
                    .post(post)
                    .userProfile(userProfile)
                    .build()
            );
            return true;
        }
    }

    private Post findPostById(long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("존재하지 않는 글입니다."));
    }

    private void checkAuthenticationForModify(Post post, UserProfile userProfile){
        // user profile equals 이걸로 하면 안될까 생각해보기
        if(userProfile == null || userProfile.getId() != post.getUserProfile().getId()) {
            throw new PostIllegalModifyException("변경할 수 있는 권한이 없습니다.");
        }
    }
}

