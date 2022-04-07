package com.inst.base.dto.chat;

import com.inst.base.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatMessagePostCreatorDTO {
    private UUID id;

    private String login;

    private String name;

    public ChatMessagePostCreatorDTO(User u) {
        if(u == null)
            return;

        this.id = u.getId();
        this.login = u.getLogin();
        this.name = u.getPersonalInformation().getName();
    }
}
