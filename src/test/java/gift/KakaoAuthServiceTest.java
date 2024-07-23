package gift.controller;

import gift.service.KakaoAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

class KakaoLoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private KakaoAuthService kakaoAuthService;

    @InjectMocks
    private KakaoLoginController kakaoLoginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(kakaoLoginController).build();
    }

    @Test
    void testKakaoLoginRedirect() throws Exception {
        mockMvc.perform(get("/kakao/login"))
                .andExpect(redirectedUrlPattern("https://kauth.kakao.com/oauth/authorize?client_id=*"));
    }

    @Test
    void testKakaoCallbackSuccess() throws Exception {
        String authorizationCode = "test-auth-code";
        String accessToken = "test-access-token";

        when(kakaoAuthService.getAccessToken(anyString())).thenReturn(accessToken);

        mockMvc.perform(get("/kakao/callback")
                        .param("code", authorizationCode))
                .andExpect(redirectedUrl("/user-products"));
    }

    @Test
    void testKakaoCallbackFailure() throws Exception {
        String authorizationCode = "test-auth-code";

        when(kakaoAuthService.getAccessToken(anyString())).thenThrow(new RuntimeException("Invalid authorization code"));

        mockMvc.perform(get("/kakao/callback")
                        .param("code", authorizationCode))
                .andExpect(redirectedUrl("/error/500"));
    }
}
