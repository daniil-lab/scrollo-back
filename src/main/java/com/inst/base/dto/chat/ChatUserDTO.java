package com.inst.base.dto.chat;

import com.inst.base.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatUserDTO {
    private UUID id;

    private String login;

    private String name;

    private String avatar;

    public ChatUserDTO(User u) {
        if(u == null)
            return;

        this.id = u.getId();
        this.login = u.getLogin();
        this.name = u.getPersonalInformation().getName();
        this.avatar = u.getAvatar();
    }
}
