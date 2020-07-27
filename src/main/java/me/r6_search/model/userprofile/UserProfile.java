package me.r6_search.model.userprofile;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.r6_search.model.comment.Comment;
import me.r6_search.model.post.Post;
import me.r6_search.model.postrecommend.PostRecommend;
import me.r6_search.model.userrole.UserRole;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String email;

    private String password;

    private String emailAuthenticateCode;

    private boolean isEmailAuthenticated;

    private boolean isUbiAuthenticated;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "roles")
    private List<UserRole> roles;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<Post> postList;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<PostRecommend> recommendList;

    @CreatedDate
    private LocalDateTime createdTime;

    public UserProfile() { }

    @Builder
    public UserProfile(String username, String email, String password, String emailCode) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.emailAuthenticateCode = emailCode;
        this.isEmailAuthenticated = false;
        this.isUbiAuthenticated = false;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public void setEmailAuthenticated(boolean isEmailAuthenticated) {
        this.isEmailAuthenticated = isEmailAuthenticated;
    }

    public void setUbiAuthenticated(boolean isUbiAuthenticated) {
        this.isUbiAuthenticated = isUbiAuthenticated;
    }
}
