package com.inst.base.dto.post;

import com.inst.base.entity.post.PostFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PostFileDTO {
    private UUID id;

    private String path;

    private String filePath;

    public PostFileDTO(PostFile pf) {
        if(pf == null)
            return;

        this.id = pf.getId();
        this.path = pf.getPath();
        this.filePath = pf.getFilePath();
    }
}
