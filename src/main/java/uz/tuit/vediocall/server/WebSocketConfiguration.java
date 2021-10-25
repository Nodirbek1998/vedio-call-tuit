package uz.tuit.vediocall.server;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author Mamadaliyev Nodirbek
 * @created 10/19/21 - 10:46 AM
 */

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
       // registry.addHandler(new SocketHandler(), "/videochat").setAllowedOrigins("*");
    }
}
