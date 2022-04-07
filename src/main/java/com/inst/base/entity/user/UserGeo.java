package com.inst.base.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserGeo {
    private Double longitude;

    private Double latitude;
}
