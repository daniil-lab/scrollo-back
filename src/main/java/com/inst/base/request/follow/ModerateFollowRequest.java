package com.inst.base.request.follow;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ModerateFollowRequest {
    @NotNull
    private UUID followId;

    @NotNull
    private ModerateFollowAction action;
}
