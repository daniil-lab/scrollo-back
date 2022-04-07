package com.inst.base.dto.chat;

import com.inst.base.entity.post.Post;
import com.inst.base.entity.post.PostType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatMessagePostDTO {
    private UUID id;

    private String content;

    private String preview;

    private PostType type;

    private ChatMessagePostCreatorDTO creator;

    public ChatMessagePostDTO(Post p) {
        if(p == null)
            return;

        this.id = p.getId();
        this.type = p.getType();
        this.content = p.getContent();
        this.preview = p.getFiles() != null ? p.getFiles().size() != 0 ? p.getFiles().stream().toList().get(0).getPath() : null : null;
        this.creator = p.getCreator() == null ? null : new ChatMessagePostCreatorDTO(p.getCreator());
    }
}
