package com.inst.base.dto.post;

import com.inst.base.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PostUserDTO {
    private UUID id;

    private String login;

    private String name;

    private String avatar;

    private String region;

    public PostUserDTO(User u) {
        this.id = u.getId();
        this.login = u.getLogin();
        this.name = u.getPersonalInformation().getName();
        this.avatar = u.getAvatar();
        this.region = u.getPersonalInformation().getRegion();
    }
}
