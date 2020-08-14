package me.r6_search.dto.post;

import lombok.Data;
import me.r6_search.model.post.Post;
import me.r6_search.model.userprofile.UserProfile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PostSaveRequestDto {
    @NotBlank
    String title;
    @NotBlank
    String content;
    String type;
    List<String> imgSrcList;

    public Post toEntity(UserProfile userProfile) {
        return Post.builder()
                .content(content)
                .title(title)
                .type(type)
                .userProfile(userProfile)
                .build();
    }
}
