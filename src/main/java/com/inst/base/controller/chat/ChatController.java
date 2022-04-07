package com.inst.base.controller.chat;

import com.inst.base.dto.chat.ChatDTO;
import com.inst.base.dto.chat.ChatMessageDTO;
import com.inst.base.request.PageRequestParams;
import com.inst.base.request.chat.CreateChatRequest;
import com.inst.base.request.chat.SendMessageRequest;
import com.inst.base.service.chat.ChatService;
import com.inst.base.util.ApiResponse;
import com.inst.base.util.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/")
    public ResponseEntity<ChatDTO> createChat(
            @Valid
            @RequestBody
                CreateChatRequest request
    ) {

        return new ResponseEntity<>(new ChatDTO(chatService.createChat(request)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/{chatId}")
    public ResponseEntity<ChatDTO> removeChat(
            @PathVariable
                    UUID userId
    ) {

        return new ResponseEntity<>(new ChatDTO(chatService.removeChat(userId)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("/message")
    public ResponseEntity<ChatMessageDTO> sendMessage(
            @Valid
            @RequestBody
                    SendMessageRequest request
    ) {

        return new ResponseEntity<>(new ChatMessageDTO(chatService.sendMessage(request)), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("/message/{messageId}")
    public ResponseEntity<ChatMessageDTO> removeMessage(
            @PathVariable
                    UUID messageId
    ) {

        return new ResponseEntity<>(new ChatMessageDTO(chatService.removeMessage(messageId)), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/message/{chatId}")
    public ResponseEntity<PageResponse<ChatMessageDTO>> getChatMessages(
            @PathVariable
                    UUID chatId,
                    PageRequestParams page
    ) {

        return new ResponseEntity<>(chatService.getChatMessages(chatId, page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/")
    public ResponseEntity<PageResponse<ChatDTO>> getChats(
            PageRequestParams page
    ) {

        return new ResponseEntity<>(chatService.getChats(page), HttpStatus.OK);
    }
}
