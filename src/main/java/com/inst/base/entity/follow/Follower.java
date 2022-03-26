package com.inst.base.entity.follow;

import com.inst.base.entity.abstracted.TimeAudit;
import com.inst.base.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Follower extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "followOnUser_id")
    private User followOnUser;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "followed_id")
    private User followedUser;
}
