package gift.service;

import gift.model.Member;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductOptionService productOptionService;
    private final WishService wishService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductOptionService productOptionService, WishService wishService) {
        this.orderRepository = orderRepository;
        this.productOptionService = productOptionService;
        this.wishService = wishService;
    }

    @Transactional
    public Order placeOrder(Member member, Long optionId, int quantity, String message) {
        ProductOption productOption = productOptionService.findProductOptionById(optionId);
        if (productOption == null) {
            throw new IllegalArgumentException("Invalid product option");
        }
        productOptionService.subtractProductOptionQuantity(optionId, quantity);
        wishService.removeWishIfExists(member.getId(), productOption.getProduct().getId(), optionId);

        Order order = new Order();
        order.setMember(member);
        order.setProductOption(productOption);
        order.setQuantity(quantity);
        order.setOrderDateTime(LocalDateTime.now());
        order.setMessage(message);
        return orderRepository.save(order);
    }
}
