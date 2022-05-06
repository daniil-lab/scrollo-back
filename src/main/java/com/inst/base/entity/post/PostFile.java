package com.inst.base.entity.post;

import com.inst.base.entity.abstracted.TimeAudit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PostFile extends TimeAudit {
    @Id
    private UUID id = UUID.randomUUID();

    private String path;

    private String filePath;

    private PostFileType type;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "post_id")
    private Post post;
}
