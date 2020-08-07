package me.r6_search.model.imgsrc;

import lombok.Builder;
import lombok.Getter;
import me.r6_search.model.post.Post;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
public class ImgSrc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToOne
    @JoinColumn(name="post_id")
    Post post;

    @NotNull
    String src;

    public ImgSrc() {}

    @Builder
    public ImgSrc(Post post, String src) {
        this.post = post;
        this.src = src;
    }

    public void updatePost(Post post) {
        this.post = post;
    }
}
