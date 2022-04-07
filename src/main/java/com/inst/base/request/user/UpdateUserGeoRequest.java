package com.inst.base.request.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateUserGeoRequest {
    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;
}
