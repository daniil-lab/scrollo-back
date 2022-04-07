package com.inst.base.dto.chat;

import com.inst.base.entity.chat.ChatMessageAttachment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatMessageAttachmentDTO {
    private UUID id;

    private String path;

    public ChatMessageAttachmentDTO(ChatMessageAttachment cma) {
        if(cma == null)
            return;

        this.id = cma.getId();
        this.path = cma.getResourcePath();
    }
}
