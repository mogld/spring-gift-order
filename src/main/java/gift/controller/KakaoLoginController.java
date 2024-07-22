package gift.controller;

import gift.service.KakaoAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class KakaoLoginController {

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @GetMapping("/kakao/callback")
    public String kakaoCallback(@RequestParam String code, Model model, HttpSession session) {
        String accessToken = kakaoAuthService.getAccessToken(code);
        session.setAttribute("accessToken", accessToken);
        model.addAttribute("accessToken", accessToken);
        return "redirect:/user-products";
    }
}
