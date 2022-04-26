package com.inst.base.request.auth;

import lombok.Data;

import java.util.UUID;

@Data
public class SubmitEmailConfirmationRequest {
    private UUID confirmationId;

    private String code;
}
