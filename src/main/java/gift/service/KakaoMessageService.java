package gift.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoMessageService {

    @Value("${kakao.api-url}")
    private String apiUrl;

    @Value("${kakao.admin-key}")
    private String adminKey;

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(KakaoMessageService.class);

    public KakaoMessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String accessToken, String message) {
        HttpHeaders headers = createHeaders(accessToken);
        String body = createRequestBody(message);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Message sent successfully: " + response.getBody());
        } else {
            logger.error("Failed to send message: " + response.getStatusCode() + " - " + response.getBody());
        }
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Admin-Key", adminKey);
        return headers;
    }

    private String createRequestBody(String message) {
        return "template_object=" + message;
    }
}