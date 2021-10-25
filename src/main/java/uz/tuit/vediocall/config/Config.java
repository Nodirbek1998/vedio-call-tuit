package uz.tuit.vediocall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Mamadaliyev Nodirbek
 * @created 10/12/21 - 9:29 AM
 */

@Configuration
public class Config {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
