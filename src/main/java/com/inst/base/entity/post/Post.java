package com.inst.base.entity.post;

import com.inst.base.entity.abstracted.TimeAudit;
import com.inst.base.entity.place.Place;
import com.inst.base.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Post extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "post")
    private Set<PostFile> files = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "post")
    private Set<PostLike> likes = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "post")
    private Set<PostComment> comments = new LinkedHashSet<>();
}
