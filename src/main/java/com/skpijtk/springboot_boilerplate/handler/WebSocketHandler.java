package com.skpijtk.springboot_boilerplate.handler;

import org.springframework.lang.NonNull;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("Connected to WebSocket server"));
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
    }
}
