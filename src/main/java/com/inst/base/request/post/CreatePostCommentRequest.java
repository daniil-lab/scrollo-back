package com.inst.base.request.post;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CreatePostCommentRequest {
    @NotNull
    private UUID postId;

    @NotBlank
    @Length(min = 1, max = 512)
    private String comment;
}
