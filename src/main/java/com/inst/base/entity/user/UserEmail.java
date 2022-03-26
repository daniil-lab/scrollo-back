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
public class UserEmail {
    @Column(unique = true)
    private String email;

    private Boolean activated = false;
}
