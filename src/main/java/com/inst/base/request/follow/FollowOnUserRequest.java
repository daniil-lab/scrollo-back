package com.inst.base.request.follow;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FollowOnUserRequest {
    @NotNull
    private UUID userId;
}
