package me.r6_search.model.post;

import lombok.Builder;
import lombok.Getter;
import me.r6_search.model.comment.Comment;
import me.r6_search.model.imgsrc.ImgSrc;
import me.r6_search.model.postrecommend.PostRecommend;
import me.r6_search.model.userprofile.UserProfile;
import me.r6_search.dto.post.PostUpdateRequestDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Post {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;

    @ManyToOne
    @JoinColumn(name="user_profile_id")
    @NotNull
    private UserProfile userProfile;

    private boolean notice;

    private String title;

    private String content;

    private int viewCnt;

    private int recommendCnt;

    @Enumerated(EnumType.STRING)
    private PostType type;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostRecommend> recommendList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<ImgSrc> imgSrcList = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime modifiedTime;

    public Post() { }

    @Builder
    public Post(UserProfile userProfile, String title, String content, String type) {
        this.userProfile = userProfile;
        this.title = title;
        this.content = content;
        this.type = PostType.valueOf(type);
        this.viewCnt = 0;
        this.recommendCnt = 0;
    }

    public void updatePost(PostUpdateRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void addImgSrc(ImgSrc imgSrc) {
        this.imgSrcList.add(imgSrc);
    }

    public void increaseViewCnt() {
        this.viewCnt++;
    }
}
