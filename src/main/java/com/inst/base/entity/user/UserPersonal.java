package com.inst.base.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserPersonal {
    private String region;

    private String bio;

    private String name;

    private UserGender gender;

    private String website;

    // Fix null embedded entity
    @JsonIgnore
    private Boolean init = true;
}
