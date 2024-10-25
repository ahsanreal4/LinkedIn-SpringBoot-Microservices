package com.linkedIn.chat_service.repository;

import com.linkedIn.chat_service.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findBySenderIdOrReceiverId(long senderId, long receiverId);
}
