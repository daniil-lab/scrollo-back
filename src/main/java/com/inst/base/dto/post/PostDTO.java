package com.inst.base.dto.post;

import com.inst.base.dto.place.PlaceDTO;
import com.inst.base.dto.user.UserDTO;
import com.inst.base.entity.place.Place;
import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostLike;
import com.inst.base.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PostDTO {
    private UUID id;

    private String content;

    private UserDTO creator;

    private PlaceDTO place;

    private Integer likesCount;

    private Integer commentsCount;

    private Set<PostFileDTO> files;

    private Set<PostCommentDTO> lastComments;

    private Set<PostLikeDTO> lastLikes;

    public PostDTO(Post p) {
        if(p == null)
            return;

        this.id = p.getId();
        this.content = p.getContent();
        this.creator = p.getCreator() == null ? null : new UserDTO(p.getCreator());
        this.place = p.getPlace() == null ? null : new PlaceDTO(p.getPlace());
        this.likesCount = p.getLikes().size();
        this.commentsCount = p.getComments().size();
        this.files = p.getFiles().stream().map(PostFileDTO::new).collect(Collectors.toSet());

        if(p.getComments().size() > 3)
            this.lastComments = p.getComments().stream().limit(3).map(PostCommentDTO::new).collect(Collectors.toSet());
        else
            this.lastComments = p.getComments().stream().map(PostCommentDTO::new).collect(Collectors.toSet());

        if(p.getLikes().size() > 3)
            this.lastLikes = p.getLikes().stream().limit(3).map(PostLikeDTO::new).collect(Collectors.toSet());
        else
            this.lastLikes = p.getLikes().stream().map(PostLikeDTO::new).collect(Collectors.toSet());
    }
}
