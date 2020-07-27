package me.r6_search.dto.post;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TopicSummaryDto {
    PageMetaDto meta;
    List<PostSummaryDto> postList;
}
