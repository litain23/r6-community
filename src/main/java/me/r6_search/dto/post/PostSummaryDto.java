package me.r6_search.dto.post;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class PostSummaryDto {
    long postId;
    String title;
    String author;
    int commentCnt;
    int recommendCnt;
    int viewCnt;
    boolean hasImg;
    boolean isNotice;
    String imgSrc; // thumbnail 사진 ? 필요가 있을까
    LocalDateTime createdTime;
}
