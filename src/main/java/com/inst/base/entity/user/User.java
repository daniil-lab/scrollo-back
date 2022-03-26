package com.inst.base.entity.user;

import com.inst.base.entity.abstracted.TimeAudit;
import com.inst.base.entity.follow.FollowRequest;
import com.inst.base.entity.follow.Follower;
import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostComment;
import com.inst.base.entity.post.PostLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import javax.annotation.RegEx;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "users")
public class User extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    @NotBlank
    @Length(min = 3, max = 48)
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String login;

    @NotBlank
    private String password;

    private UserRole role = UserRole.ROLE_USER;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "emailData_email")),
            @AttributeOverride(name = "activated", column = @Column(name = "emailData_activated"))
    })
    private UserEmail emailData = new UserEmail();

    @Embedded
    private UserPersonal personalInformation = new UserPersonal();

    private String avatar;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "phone", column = @Column(name = "phoneData_phone")),
            @AttributeOverride(name = "activated", column = @Column(name = "phoneData_activated"))
    })
    private UserPhone phoneData = new UserPhone();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "followOnUser", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Follower> followers = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "followedUser", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Follower> followed = new LinkedHashSet<>();;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "followingReceiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FollowRequest> followingRequests = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "followingRequester", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FollowRequest> sentFollowingRequests = new LinkedHashSet<>();;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Transient
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "likedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostLike> likes = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sentUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostComment> comments = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "users_savedPosts",
            joinColumns = @JoinColumn(name = "User_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "savedPosts_id", referencedColumnName = "id"))
    @Fetch(FetchMode.SUBSELECT)
    private Set<Post> savedPosts = new LinkedHashSet<>();

    private AccountType accountType = AccountType.OPEN;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
