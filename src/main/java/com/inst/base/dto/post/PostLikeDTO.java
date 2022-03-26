package com.inst.base.dto.post;

import com.inst.base.dto.user.UserDTO;
import com.inst.base.entity.post.PostLike;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PostLikeDTO {
    private UUID id;

    private UserDTO user;

    public PostLikeDTO(PostLike pl) {
        if(pl == null)
            return;

        this.id = pl.getId();
        this.user = new UserDTO(pl.getLikedUser());
    }
}
