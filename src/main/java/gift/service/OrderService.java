package gift.service;

import gift.model.Member;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private WishService wishService;

    @Autowired
    private KakaoMessageService kakaoMessageService;

    @Autowired
    private HttpSession session;

    @Transactional
    public Order createOrder(Long optionId, int quantity, String message, Member member) {
        ProductOption productOption = productOptionService.findProductOptionById(optionId);
        if (productOption == null) {
            throw new IllegalArgumentException("Invalid product option");
        }

        productOptionService.subtractProductOptionQuantity(optionId, quantity);

        Order order = new Order();
        order.setProductOption(productOption);
        order.setQuantity(quantity);
        order.setMessage(message);
        order.setMember(member);
        order.setOrderDateTime(LocalDateTime.now());

        wishService.deleteWishByProductOptionIdAndMemberId(optionId, member.getId());

        Order savedOrder = orderRepository.save(order);


        String accessToken = (String) session.getAttribute("accessToken");
        if (accessToken != null) {
            kakaoMessageService.sendMessage(accessToken, createKakaoMessage(order));
        } else {

            System.out.println("No access token available for Kakao message");
        }

        return savedOrder;
    }

    private String createKakaoMessage(Order order) {
        return String.format("{\"object_type\":\"text\",\"text\":\"Order placed: %d of %s\",\"link\":{\"web_url\":\"http://localhost:8080/user-products\"}}",
                order.getQuantity(), order.getProductOption().getName());
    }
}
