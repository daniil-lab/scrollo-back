package com.inst.base.request.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class EmailAuthRequest {
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$")
    private String password;
}
