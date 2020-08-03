package me.r6_search.dto.post;

import lombok.Data;
import me.r6_search.model.post.Post;
import me.r6_search.model.post.PostType;
import me.r6_search.model.userprofile.UserProfile;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostSaveRequestDto {
    String title;
    String content;
    String type;
    MultipartFile[] files;

    public Post toEntity(UserProfile userProfile) {
        return Post.builder()
                .content(content)
                .title(title)
                .type(type)
                .userProfile(userProfile)
                .build();
    }
}
