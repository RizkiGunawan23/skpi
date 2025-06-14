package com.skpijtk.springboot_boilerplate.config;

import com.skpijtk.springboot_boilerplate.handler.WebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), "/ws").setAllowedOrigins("*");
    }
}
