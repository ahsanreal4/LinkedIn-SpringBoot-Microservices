package com.linkedIn.chat_service.controller;

import com.linkedIn.chat_service.dto.ChatDto;
import com.linkedIn.chat_service.dto.CreateChatDto;
import com.linkedIn.chat_service.dto.CreateMessageDto;
import com.linkedIn.chat_service.dto.UpdateMessageDto;
import com.linkedIn.chat_service.service.ChatService;
import com.linkedIn.chat_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final UserUtils userUtils;
    private final ChatService chatService;

    public ChatController(ChatService chatService, UserUtils userUtils) {
        this.userUtils = userUtils;
        this.chatService = chatService;
    }

    @PostMapping("")
    public void createChat(@Valid @RequestBody CreateChatDto createChatDto, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        this.chatService.createChat(createChatDto, userId);
    }

    @PostMapping("/messages")
    public void createMessage(@Valid @RequestBody CreateMessageDto createMessageDto) {
        this.chatService.createMessage(createMessageDto);
    }

    @PutMapping("/messages/{id}")
    public void updateMessage(@Valid @RequestBody UpdateMessageDto updateMessageDto, @PathVariable("id") long messageId) {
        this.chatService.updateMessage(updateMessageDto, messageId);
    }

    @GetMapping("")
    public ResponseEntity<List<ChatDto>> getAllChatsByUserId(HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        List<ChatDto> chats = this.chatService.getAllChatsByUserId(userId);

        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatDto> getChatById(@PathVariable("id") long chatId) {
        ChatDto chat = this.chatService.getChatById(chatId);

        return ResponseEntity.ok(chat);
    }

    @DeleteMapping("/{id}")
    public void deleteChatById(@PathVariable("id") long chatId, HttpServletRequest request) {
        long userId = this.userUtils.getUserId(request);
        boolean isAdmin = this.userUtils.isAdmin(request);

        this.chatService.deleteChatById(chatId, userId, isAdmin);
    }

    @DeleteMapping("/messages/{id}")
    public void deleteMessageById(@PathVariable("id") long messageId) {
        this.chatService.deleteMessageById(messageId);
    }
}
