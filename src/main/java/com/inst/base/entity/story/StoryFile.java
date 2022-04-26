package com.inst.base.entity.story;

import com.inst.base.entity.abstracted.TimeAudit;
import com.inst.base.entity.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class StoryFile extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    private String path;

    private String filePath;
}
