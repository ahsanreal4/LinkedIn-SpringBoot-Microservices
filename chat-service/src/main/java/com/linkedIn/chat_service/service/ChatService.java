package com.linkedIn.chat_service.service;

import com.linkedIn.chat_service.dto.ChatDto;
import com.linkedIn.chat_service.dto.CreateChatDto;
import com.linkedIn.chat_service.dto.CreateMessageDto;
import com.linkedIn.chat_service.dto.UpdateMessageDto;

import java.util.List;

public interface ChatService {
    void createChat(CreateChatDto createChatDto, long userId);
    void createMessage(CreateMessageDto createMessageDto, long userId);
    void updateMessage(UpdateMessageDto updateMessageDto, long messageId, long userId);
    List<ChatDto> getAllChatsByUserId(long userId);
    ChatDto getChatById(long chatId);
    void deleteChatById(long chatId, long userId, boolean isAdmin);
    void deleteMessageById(long messageId, long userId);
}
