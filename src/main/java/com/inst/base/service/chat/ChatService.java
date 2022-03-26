package com.inst.base.service.chat;

import com.inst.base.entity.chat.Chat;
import com.inst.base.entity.chat.ChatMessage;
import com.inst.base.request.chat.CreateChatRequest;
import com.inst.base.request.chat.SendMessageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ChatService {
    Chat createChat(CreateChatRequest request);

    ChatMessage sendMessage(SendMessageRequest request);

    Chat removeChat(UUID chatId);

    ChatMessage removeMessage(UUID messageId);
}
