package br.com.umc.cyclecare.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google")
@Data
public class GoogleConfig {
    private Client client = new Client();
    private Application application = new Application();

    @Data
    public static class Application {
        private String name;
    }

    @Data
    public static class Client {
        private String id;
        private String secret;
    }
}