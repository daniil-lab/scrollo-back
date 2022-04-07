package com.inst.base.service.chat;

import com.inst.base.dto.chat.ChatDTO;
import com.inst.base.dto.chat.ChatMessageDTO;
import com.inst.base.entity.chat.Chat;
import com.inst.base.entity.chat.ChatMessage;
import com.inst.base.entity.chat.ChatMessageAttachment;
import com.inst.base.entity.user.User;
import com.inst.base.repository.chat.ChatMessageAttachmentRepository;
import com.inst.base.repository.chat.ChatMessageRepository;
import com.inst.base.repository.chat.ChatRepository;
import com.inst.base.repository.user.UserRepository;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.chat.CreateChatRequest;
import com.inst.base.request.chat.SendMessageRequest;
import com.inst.base.service.FileStorageService;
import com.inst.base.util.AccessChecker;
import com.inst.base.util.AuthHelper;
import com.inst.base.util.PageResponse;
import com.inst.base.util.ServiceException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMessageAttachmentRepository chatMessageAttachmentRepository;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Chat createChat(CreateChatRequest request) {
        User requester = authHelper.getUserFromAuthCredentials();

        User user = userRepository.findById(requester.getId()).orElseThrow(() -> {
            throw new ServiceException("User not found", HttpStatus.NOT_FOUND);
        });

        if(!AccessChecker.followed(user, requester))
            throw new ServiceException("No access", HttpStatus.BAD_REQUEST);

        Chat chat = new Chat();

        chat.setStarter(requester);
        chat.setAcceptor(user);

        chatRepository.save(chat);

        return chat;
    }

    @Override
    public ChatMessage sendMessage(SendMessageRequest request) {
        User user = authHelper.getUserFromAuthCredentials();

        Chat chat = chatRepository.findById(request.getChatId()).orElseThrow(() -> {
            throw new ServiceException("Chat not found", HttpStatus.NOT_FOUND);
        });

        if(!AccessChecker.equals(user, chat.getAcceptor()) && !AccessChecker.equals(user, chat.getStarter()))
            throw new ServiceException("No access", HttpStatus.BAD_REQUEST);

        ChatMessage chatMessage = new ChatMessage();

        if(request.getAttachments() != null) {
            for (MultipartFile file : request.getAttachments()) {
                ChatMessageAttachment chatMessageAttachment = new ChatMessageAttachment();

                chatMessageAttachment.setChat(chat);
                chatMessageAttachment.setMessage(chatMessage);
                chatMessageAttachment.setResourcePath(fileStorageService.storeFile(file, user));

                chatMessageAttachmentRepository.save(chatMessageAttachment);
            }
        }

        chatMessage.setContent(request.getText());
        chatMessage.setChat(chat);
        chatMessage.setSender(user);

        chatMessageRepository.save(chatMessage);

        return chatMessage;
    }

    @Override
    @Transactional
    public Chat removeChat(UUID chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> {
            throw new ServiceException("Chat not found", HttpStatus.NOT_FOUND);
        });

        chat.setAcceptor(null);
        chat.setStarter(null);

        chatRepository.delete(chat);

        return chat;
    }

    @Override
    @Transactional
    public ChatMessage removeMessage(UUID messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId).orElseThrow(() -> {
            throw new ServiceException("Message not found", HttpStatus.NOT_FOUND);
        });

        message.setChat(null);
        message.setReceiver(null);
        message.setPost(null);
        message.setSender(null);

//        message.getAttachments().forEach(i -> {
//            fileStorageService.destroyFile(i.getResourcePath());
//            i.setMessage(null);
//            i.setChat(null);
//            chatMessageAttachmentRepository.delete(i);
//        });

        chatMessageRepository.delete(message);

        return message;
    }

    @Override
    public PageResponse<ChatDTO> getChats(PageRequestParams params) {
        User user = authHelper.getUserFromAuthCredentials();

        Page<Chat> chats = chatRepository.findByStarterIdOrAcceptorId(
                user.getId(),
                user.getId(),
                PageRequest.of(params.getPage(), params.getPageSize()));

        return new PageResponse<>(chats.getContent().stream().map(ChatDTO::new).collect(Collectors.toList()), params.getPage(), chats.getTotalPages(), chats.getTotalElements());
    }

    @Override
    public PageResponse<ChatMessageDTO> getChatMessages(UUID chatId, PageRequestParams params) {
        User user = authHelper.getUserFromAuthCredentials();

        Page<ChatMessage> messages = chatMessageRepository.findByChatIdAndSenderIdOrReceiverId(
                chatId,
                user.getId(),
                user.getId(),
                PageRequest.of(params.getPage(), params.getPageSize()));

        return new PageResponse<>(messages.getContent().stream().map(ChatMessageDTO::new).collect(Collectors.toList()), params.getPage(), messages.getTotalPages(), messages.getTotalElements());
    }
}
