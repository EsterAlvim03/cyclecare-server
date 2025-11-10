package br.com.umc.cyclecare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(maxAge = 3600)
@SpringBootApplication
public class ApiCycleCareApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiCycleCareApplication.class, args);
    }
}
