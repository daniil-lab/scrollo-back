package com.inst.base.request.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class ValidateTokenRequest {
    @NotBlank
    private String token;
}
