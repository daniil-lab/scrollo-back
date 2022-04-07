package com.inst.base.service.chat;

import com.inst.base.dto.chat.ChatDTO;
import com.inst.base.dto.chat.ChatMessageDTO;
import com.inst.base.entity.chat.Chat;
import com.inst.base.entity.chat.ChatMessage;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.chat.CreateChatRequest;
import com.inst.base.request.chat.SendMessageRequest;
import com.inst.base.util.PageResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ChatService {
    Chat createChat(CreateChatRequest request);

    ChatMessage sendMessage(SendMessageRequest request);

    Chat removeChat(UUID chatId);

    ChatMessage removeMessage(UUID messageId);

    PageResponse<ChatDTO> getChats(PageRequestParams params);

    PageResponse<ChatMessageDTO> getChatMessages(UUID chatId, PageRequestParams params);
}
