package com.inst.base.request.post;

import com.inst.base.entity.post.PostType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class CreatePostRequest {
    @NotBlank
    private String content;

    private String place;

    @NotNull
    private String type;

    private List<MultipartFile> files;
}
