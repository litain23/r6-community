package me.r6_search.dto.comment;

import lombok.Data;
import me.r6_search.model.comment.Comment;
import me.r6_search.model.post.Post;
import me.r6_search.model.userprofile.UserProfile;

@Data
public class CommentSaveRequestDto {
    long postId;
    String content;
    long parentCommentId;

    public Comment toEntity(Post post, UserProfile userProfile) {
        return Comment.builder()
                .content(content)
                .userProfile(userProfile)
                .post(post)
                .build();
    }

}
