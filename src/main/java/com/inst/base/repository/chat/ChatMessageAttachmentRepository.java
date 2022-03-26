package com.inst.base.repository.chat;

import com.inst.base.entity.chat.ChatMessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatMessageAttachmentRepository extends JpaRepository<ChatMessageAttachment, UUID> {
}
