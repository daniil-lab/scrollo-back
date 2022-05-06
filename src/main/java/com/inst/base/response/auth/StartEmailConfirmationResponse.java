package com.inst.base.response.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class StartEmailConfirmationResponse {
    private UUID id;

    private String code;
}
