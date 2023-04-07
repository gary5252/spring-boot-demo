package com.spring.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import jakarta.websocket

/**
 * @EnableWebSocketMessageBroker：允許啟用訊息代理(message broker)
 * @WebSocketMessageBrokerConfigurer：定義簡單訊息協定一些方法的介面，
 * 如 STOMP(Simple Text Orientated Messaging Protocol)是一種基於 WebSocket
 * 的簡單訊息傳輸協定，它定義了一些操作 WebSocket 的方法，包含 CONNECT、SEDN、SUBSCRIBE 等
 * registerStompEndpoints()：用來定義建立連線的節點(endpoint)，可以想成是在定義連線的 url
 * .withSockJS()：指定使用 SockJS 協定，SockJs 是 JavaScript 使用 WebSocket 技術的 Library，
 * 簡單來說，我們允許讓 JS 使用 WebScoket 相關操作
 * configureMessageBroker()：用來定義訊息代理的前綴(prefix)，可以想成是在定義代理的 url 
 * 這邊我們定義代理的前綴都是 /topic
 */

@Configuration
@EnableWebSocketMessageBroker	
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
    }

}
