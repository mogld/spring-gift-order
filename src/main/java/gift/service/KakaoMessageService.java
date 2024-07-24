package gift.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoMessageService {

    @Value("${kakao.api-url}")
    private String apiUrl;

    @Value("${kakao.admin-key}")
    private String adminKey;

    public void sendMessage(String accessToken, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Admin-Key", adminKey);
        }
    }
}