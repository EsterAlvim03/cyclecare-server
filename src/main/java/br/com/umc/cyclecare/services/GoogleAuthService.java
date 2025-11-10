package br.com.umc.cyclecare.services;

import br.com.umc.cyclecare.config.GoogleConfig;
import br.com.umc.cyclecare.dtos.GoogleUserInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final GoogleConfig googleConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GoogleUserInfo verifyAccessToken(String accessToken) throws IOException {
        String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + accessToken;

        String response = restTemplate.getForObject(url, String.class);
        JsonNode jsonNode = objectMapper.readTree(response);

        String aud = jsonNode.get("aud").asText();
        if (!aud.equals(googleConfig.getClient().getId())) {
            System.out.println(googleConfig.getClient().getId());
            throw new IllegalArgumentException("Token não pertence a este aplicativo");
        }

        return getUserInfo(accessToken);
    }

    public GoogleUserInfo getUserInfo(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfo> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                GoogleUserInfo.class
        );

        return response.getBody();
    }
}