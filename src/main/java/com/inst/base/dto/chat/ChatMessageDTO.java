package com.inst.base.dto.chat;

import com.inst.base.entity.chat.ChatMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ChatMessageDTO {
    private UUID id;

    private String content;

    private ChatUserDTO sender;

    private ChatUserDTO receiver;

    private List<ChatMessageAttachmentDTO> attachments;

    private ChatMessagePostDTO post;

    private Instant createdAt;

    private Instant updatedAt;

    public ChatMessageDTO(ChatMessage cm) {
        if(cm == null)
            return;

        this.id = cm.getId();
        this.content = cm.getContent();
        this.sender = cm.getSender() == null ? null : new ChatUserDTO(cm.getSender());
        this.receiver = cm.getReceiver() == null ? null : new ChatUserDTO(cm.getReceiver());
        this.attachments = cm.getAttachments().stream().map(ChatMessageAttachmentDTO::new).collect(Collectors.toList());
        this.post = cm.getPost() == null ? null : new ChatMessagePostDTO(cm.getPost());
        this.createdAt = cm.getCreatedAt();
        this.updatedAt = cm.getUpdatedAt();
    }
}
