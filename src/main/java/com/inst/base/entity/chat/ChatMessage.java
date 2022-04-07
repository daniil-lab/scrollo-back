package com.inst.base.entity.chat;

import com.inst.base.entity.abstracted.TimeAudit;
import com.inst.base.entity.post.Post;
import com.inst.base.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ChatMessage extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "starter_id")
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chat", cascade = CascadeType.ALL)
    private Set<ChatMessageAttachment> attachments;
}
