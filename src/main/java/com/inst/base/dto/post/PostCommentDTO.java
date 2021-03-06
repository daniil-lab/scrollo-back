package com.inst.base.dto.post;

import com.inst.base.dto.user.UserDTO;
import com.inst.base.entity.post.PostComment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PostCommentDTO {
    private UUID id;

    private String comment;

    private PostUserDTO user;

    private Integer likesCount;

    public PostCommentDTO(PostComment pc) {
        if(pc == null)
            return;

        this.id = pc.getId();
        this.comment = pc.getContent();
        this.user = pc.getSentUser() == null ? null : new PostUserDTO(pc.getSentUser());
        this.likesCount = pc.getLikes().size();
    }

}
