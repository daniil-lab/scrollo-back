package com.inst.base.dto.follow;

import com.inst.base.dto.user.UserDTO;
import com.inst.base.entity.follow.Follower;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FollowerDTO {
    private UUID id;

    private UserDTO followOnUser;

    private UserDTO followedUser;

    private Instant createAt;

    private Instant updatedAt;

    public FollowerDTO(Follower f) {
        if(f == null)
            return;

        this.id = f.getId();
        this.followedUser = new UserDTO(f.getFollowedUser());
        this.followOnUser = new UserDTO(f.getFollowOnUser());
        this.createAt = f.getCreatedAt();
        this.updatedAt = f.getUpdatedAt();
    }
}
