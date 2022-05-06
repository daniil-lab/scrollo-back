package com.inst.base.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
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

    private String background;

    private UserPersonalDTO personal;

    private String avatar;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "UTC")
    private Instant createAt;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ",timezone = "UTC")
    private Instant updatedAt;

    private Integer postCount;

    private Integer followersCount;

    private Integer followingCount;

    private AccountType type;

    private UserDirectionDTO direction;

    private String career;

    public UserDTO(User u) {
        if(u == null)
            return;

        this.direction = u.getDirection() == null ? null : new UserDirectionDTO(u.getDirection());
        this.id = u.getId();
        this.login = u.getLogin();
        this.email = u.getEmailData().getEmail();
        this.phone = u.getPhoneData().getPhone();
        this.personal = u.getPersonalInformation() == null ? null : new UserPersonalDTO(u.getPersonalInformation());
        this.createAt = u.getCreatedAt();
        this.updatedAt = u.getUpdatedAt();
        this.avatar = u.getAvatar();
        this.postCount = u.getPosts().size();
        this.followersCount = u.getFollowers().size();
        this.followingCount = u.getFollowed().size();
        this.type = u.getAccountType();
        this.background = u.getBackground();
        this.career = u.getCareer();
    }
}
