package com.inst.base.entity.follow;

import com.inst.base.entity.abstracted.TimeAudit;
import com.inst.base.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FollowRequest extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "followingRequester_id")
    private User followingRequester;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "followingReceiver_id")
    private User followingReceiver;
}
