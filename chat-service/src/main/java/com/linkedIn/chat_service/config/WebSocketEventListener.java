package com.linkedIn.chat_service.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketEventListener {

    private final String ID_KEY = "x-user-id";

    private final SimpUserRegistry userRegistry;
    private final Map<String, String> userSessionMap = new HashMap<>();

    public WebSocketEventListener(SimpUserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String userId = headers.getNativeHeader(ID_KEY).get(0);
        userSessionMap.put(headers.getSessionId(), userId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headers.getSessionId();
        String userId = userSessionMap.get(sessionId);
        userSessionMap.remove(sessionId);
    }
}

