package com.inst.base.dto.user;

import com.inst.base.entity.user.AccountType;
import com.inst.base.entity.user.User;
import com.inst.base.entity.user.UserGender;
import com.inst.base.entity.user.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDTO {
    private UUID id;

    private String login;

    private String email;

    private String phone;

    private UserPersonalDTO personal;

    private String avatar;

    private Instant createAt;

    private Instant updatedAt;

    private Integer postCount;

    private Integer followersCount;

    private Integer followingCount;

    private AccountType type;

    public UserDTO(User u) {
        if(u == null)
            return;

        this.id = u.getId();
        this.login = u.getLogin();
        this.email = u.getEmailData().getEmail();
        this.phone = u.getPhoneData().getPhone();
        this.personal = new UserPersonalDTO(u.getPersonalInformation());
        this.createAt = u.getCreatedAt();
        this.updatedAt = u.getUpdatedAt();
        this.avatar = u.getAvatar();
        this.postCount = u.getPosts().size();
        this.followersCount = u.getFollowers().size();
        this.followingCount = u.getFollowed().size();
        this.type = u.getAccountType();
    }
}
