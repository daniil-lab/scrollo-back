package com.inst.base.request.post;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CreateDislikePostRequest {
    @NotNull
    private UUID postId;
}
