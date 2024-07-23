package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.OrderRequest;
import gift.model.Member;
import gift.model.Order;
import gift.service.KakaoAuthService;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private KakaoAuthService kakaoAuthService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@LoginMember Member member, @RequestBody OrderRequest orderRequest, HttpSession session) {
        Order order = orderService.createOrder(member, orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage());
        String accessToken = (String) session.getAttribute("accessToken");
        String message = createKakaoMessage(order);
        kakaoAuthService.sendKakaoMessage(accessToken, message);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    private String createKakaoMessage(Order order) {
        return "{ \"object_type\": \"text\", \"text\": \"주문이 완료되었습니다.\", \"link\": { \"web_url\": \"http://localhost:8080\", \"mobile_web_url\": \"http://localhost:8080\" }, \"button_title\": \"주문 확인\" }";
    }
}
