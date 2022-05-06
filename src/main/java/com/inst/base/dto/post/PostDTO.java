package com.inst.base.dto.post;

import com.inst.base.dto.place.PlaceDTO;
import com.inst.base.dto.user.UserDTO;
import com.inst.base.entity.place.Place;
import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostLike;
import com.inst.base.entity.post.PostType;
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

    private PostUserDTO creator;

    private PlaceDTO place;

    private PostType type;

    private Integer likesCount;

    private Integer commentsCount;

    private Integer dislikeCount;

    private Set<PostFileDTO> files;

    private Set<PostCommentDTO> lastComments;

    private Set<PostLikeDTO> lastLikes;

    private Boolean liked = false;

    private Boolean commented = false;

    private Boolean disliked = false;

    private Boolean inSaved = false;

    public PostDTO(Post p) {
        if(p == null)
            return;

        this.id = p.getId();
        this.content = p.getContent();
        this.creator = p.getCreator() == null ? null : new PostUserDTO(p.getCreator());
        this.place = p.getPlace() == null ? null : new PlaceDTO(p.getPlace());
        this.likesCount = p.getLikes().size();
        this.commentsCount = p.getComments().size();
        this.dislikeCount = p.getDislikes().size();
        this.files = p.getFiles().stream().map(PostFileDTO::new).collect(Collectors.toSet());
        this.type = p.getType();

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
