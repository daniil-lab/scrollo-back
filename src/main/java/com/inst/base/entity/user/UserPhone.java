package com.inst.base.entity.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class UserPhone {
    @Column(unique = true)
    private String phone;

    private Boolean activated = false;
}
