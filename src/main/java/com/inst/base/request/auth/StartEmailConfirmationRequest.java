package com.inst.base.request.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StartEmailConfirmationRequest {
    @NotBlank
    private String email;
}
