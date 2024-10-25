package com.linkedIn.chat_service.repository;

import com.linkedIn.chat_service.entity.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatId(long chatId);
}
