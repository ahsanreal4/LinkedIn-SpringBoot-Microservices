package com.linkedIn.chat_service.service.impl;

import com.linkedIn.chat_service.dto.*;
import com.linkedIn.chat_service.entity.chat.Chat;
import com.linkedIn.chat_service.entity.chat.Message;
import com.linkedIn.chat_service.entity.user.User;
import com.linkedIn.chat_service.exception.ApiException;
import com.linkedIn.chat_service.feign_clients.impl.UserServiceClientImpl;
import com.linkedIn.chat_service.repository.ChatRepository;
import com.linkedIn.chat_service.repository.MessageRepository;
import com.linkedIn.chat_service.service.ChatService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

//    private final SimpMessagingTemplate messagingTemplate;

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserServiceClientImpl userServiceClient;

    public ChatServiceImpl(ChatRepository chatRepository, MessageRepository messageRepository,
                           UserServiceClientImpl userServiceClient) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public void createChat(CreateChatDto createChatDto, long userId) {
        if (userId == createChatDto.getReceiverId()) throw new ApiException(HttpStatus.BAD_REQUEST, "sender and receiver id cannot be the same");

        User user = new User();
        user.setId(userId);

        returnUserOrThrowError(createChatDto.getReceiverId());

        User receiver = new User();
        receiver.setId(createChatDto.getReceiverId());

        Chat chat = new Chat();
        chat.setSender(user);
        chat.setReceiver(receiver);
        chat.setCreatedAt(new Date());

        try{
            chatRepository.save(chat);
        }
        catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, "chat with this sender and receiver already exists");
        }
    }

    @Override
    public void createMessage(CreateMessageDto createMessageDto, long userId) {
        Chat chat = returnChatOrThrowError(createMessageDto.getChatId());

        long senderId = chat.getSender().getId();
        long receiverId = chat.getReceiver().getId();

        long messageReceiverId;

        if (userId == senderId) {
            messageReceiverId = receiverId;
        }
        else {
            messageReceiverId = senderId;
        }

        Message message = new Message();
        message.setChat(chat);
        message.setText(createMessageDto.getText());
        message.setSentAt(new Date());
        message.setSenderId(userId);
        message.setReceiverId(messageReceiverId);

//        messagingTemplate.convertAndSendToUser(String.valueOf(messageReceiverId), "/queue/messages", message);

        messageRepository.save(message);
    }

    @Override
    public void updateMessage(UpdateMessageDto updateMessageDto, long messageId, long userId) {
        Message message = returnMessageOrThrowError(messageId);
        if (message.getSenderId() != userId) throw new ApiException(HttpStatus.BAD_REQUEST, "you cannot update someone else message");

        message.setText(updateMessageDto.getText());

        messageRepository.save(message);
    }

    @Override
    public void deleteMessageById(long messageId, long userId) {
        Message message = returnMessageOrThrowError(messageId);
        if (message.getSenderId() != userId) throw new ApiException(HttpStatus.BAD_REQUEST, "you cannot delete someone else message");

        messageRepository.delete(message);
    }

    @Override
    public List<ChatDto> getAllChatsByUserId(long userId) {
        List<Chat> chats = this.chatRepository.findBySenderIdOrReceiverId(userId, userId);

        return chats.stream().map(chat -> getChatData(chat, false)).collect(Collectors.toList());
    }

    @Override
    public ChatDto getChatById(long chatId) {
        Chat chat = returnChatOrThrowError(chatId);

        return getChatData(chat, true);
    }

    @Override
    public void deleteChatById(long chatId, long userId, boolean isAdmin) {
        Chat chat = returnChatOrThrowError(chatId);

        long senderId = chat.getSender().getId();
        long receiverId = chat.getReceiver().getId();

        if (userId != senderId && userId != receiverId && !isAdmin) throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot delete others chat");

        chatRepository.delete(chat);
    }

    private ChatDto getChatData(Chat chat, boolean fetchMessages) {
        ChatDto dto = new ChatDto();
        dto.setId(chat.getId());

        UserProfileDto sender = returnUserProfileOrThrowError(chat.getSender().getId());
        UserProfileDto receiver = returnUserProfileOrThrowError(chat.getReceiver().getId());

        dto.setSender(sender);
        dto.setReceiver(receiver);
        dto.setCreatedAt(chat.getCreatedAt());

        if(!fetchMessages) return dto;

        List<Message> messages = this.messageRepository.findByChatId(chat.getId());
        List<MessageDto> messageDtos = messages.stream().map(this::mapToMessageDto).collect(Collectors.toList());

        dto.setMessages(messageDtos);

        return dto;
    }


    private MessageDto mapToMessageDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setText(message.getText());
        dto.setSentAt(message.getSentAt());
        dto.setSenderId(message.getSenderId());
        dto.setReceiverId(message.getReceiverId());

        return dto;
    }

    private UserDto returnUserOrThrowError(long userId) {
        try {
            return this.userServiceClient.getUserById(userId);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.NOT_FOUND, "user with id " + userId + " was not found");
        }
    }

    private UserProfileDto returnUserProfileOrThrowError(long userId){
        try {
            return this.userServiceClient.getUserProfileById(userId);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.NOT_FOUND, "user profile with id " + userId + " was not found");
        }
    }

    private Chat returnChatOrThrowError(long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "chat with id " + chatId + " was not found"));
    }

    private Message returnMessageOrThrowError (long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "message with id " + messageId + " was not found"));
    }
}
