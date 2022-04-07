package com.inst.base.dto.chat;

import com.inst.base.entity.chat.Chat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatDTO {
    private UUID id;

    private ChatUserDTO starter;

    private ChatUserDTO receiver;

    public ChatDTO(Chat c) {
        if(c == null)
            return;

        this.id = c.getId();
        this.starter = c.getStarter() == null ? null : new ChatUserDTO(c.getStarter());
        this.receiver = c.getAcceptor() == null ? null : new ChatUserDTO(c.getAcceptor());
    }
}
