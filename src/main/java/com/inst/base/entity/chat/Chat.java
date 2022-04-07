package com.inst.base.entity.chat;

import com.inst.base.entity.abstracted.TimeAudit;
import com.inst.base.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Chat extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "starter_id")
    private User starter;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "acceptor_id")
    private User acceptor;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "chat")
    private Set<ChatMessage> messages = new LinkedHashSet<>();
}
