package com.inst.base.entity.action;

import com.inst.base.entity.post.Post;
import com.inst.base.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Action {
    @Id
    private UUID id = UUID.randomUUID();

    private ActionType action;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.DETACH
    })
    @JoinColumn(name = "owner_id")
    private User receiver;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.DETACH
    })
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.DETACH
    })
    @JoinColumn(name = "post_id")
    private Post post;

    private Boolean viewedByReceiver = false;
}
