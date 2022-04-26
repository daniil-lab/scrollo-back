package com.inst.base.request.auth;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
public class SignInByEmailConfirmationRequest {
    @NotBlank
    @Length(min = 3, max = 48)
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String login;

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$")
    private String password;

    private UUID confirmationId;
}
