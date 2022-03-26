package com.inst.base.config;

import com.inst.base.entity.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AuthCredentials {
    private String login;

    private UUID id;
}
