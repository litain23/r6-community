package me.r6_search.dto.post;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageMetaDto {
    long currentPage;
    long totalPage;
}
