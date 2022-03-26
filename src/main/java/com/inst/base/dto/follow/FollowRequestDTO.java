package com.inst.base.dto.follow;

import com.inst.base.dto.user.UserDTO;
import com.inst.base.entity.follow.FollowRequest;
import com.inst.base.entity.follow.Follower;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FollowRequestDTO {
    private UUID id;

    private UserDTO followingRequester;

    private UserDTO followingReceiver;

    private Instant createAt;

    private Instant updatedAt;

    private FollowRequestDTO(FollowRequest f) {
        if(f == null)
            return;

        this.id = f.getId();
        this.followingReceiver = new UserDTO(f.getFollowingReceiver());
        this.followingRequester = new UserDTO(f.getFollowingRequester());
        this.createAt = f.getCreatedAt();
        this.updatedAt = f.getUpdatedAt();
    }
}
