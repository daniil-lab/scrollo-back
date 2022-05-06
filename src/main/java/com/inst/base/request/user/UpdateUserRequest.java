package com.inst.base.request.user;

import com.inst.base.entity.user.AccountType;
import com.inst.base.entity.user.UserDirection;
import com.inst.base.entity.user.UserGender;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;

    private String login;

    private String email;

    private String phone;

    private String region;

    private String bio;

    private UserGender gender;

    private String website;

    private AccountType accountType;

    private UserDirection direction;

    private String career;
}
