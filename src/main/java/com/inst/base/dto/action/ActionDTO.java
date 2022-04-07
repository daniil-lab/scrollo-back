package com.inst.base.dto.action;

import com.inst.base.entity.action.Action;
import com.inst.base.entity.action.ActionType;
import com.inst.base.entity.post.Post;
import com.inst.base.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ActionDTO {
    private UUID id;

    private ActionType action;

    private ActionUserDTO receiver;

    private ActionUserDTO creator;

    private ActionPostDTO post;

    private Boolean viewedByReceiver;

    public ActionDTO(Action a) {
        if(a == null)
            return;

        this.id = a.getId();
        this.action = a.getAction();
        this.receiver = a.getReceiver() == null ? null : new ActionUserDTO(a.getReceiver());
        this.creator = a.getCreator() == null ? null : new ActionUserDTO(a.getCreator());
        this.post = a.getPost() == null ? null : new ActionPostDTO(a.getPost());
        this.viewedByReceiver = a.getViewedByReceiver();
    }
}
