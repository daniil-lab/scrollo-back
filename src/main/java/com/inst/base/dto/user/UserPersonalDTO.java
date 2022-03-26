package com.inst.base.dto.user;

import com.inst.base.entity.user.UserGender;
import com.inst.base.entity.user.UserPersonal;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserPersonalDTO {
    private String region;

    private String bio;

    private String name;

    private UserGender gender;

    private String website;

    public UserPersonalDTO(UserPersonal p) {
        this.region = p.getRegion();
        this.bio = p.getBio();
        this.name = p.getName();
        this.gender = p.getGender();
        this.website = p.getWebsite();
    }
}
