package br.com.umc.cyclecare.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    static {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );

        System.out.println("✅ .env carregado manualmente!");
        System.out.println("DATABASE_URL = " + System.getProperty("DATABASE_URL"));
    }
}
