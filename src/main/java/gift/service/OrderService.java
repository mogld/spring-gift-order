package gift.service;

import gift.model.Member;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.OrderRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private WishRepository wishRepository;

    @Transactional
    public Order createOrder(Member member, Long optionId, int quantity, String message) {
        ProductOption productOption = productOptionService.findProductOptionById(optionId);
        if (productOption == null) {
            throw new IllegalArgumentException("Invalid product option");
        }
        productOption.subtractQuantity(quantity);
        productOptionService.saveProductOption(productOption);

        Order order = new Order();
        order.setProductOption(productOption);
        order.setMember(member);
        order.setQuantity(quantity);
        order.setOrderDateTime(LocalDateTime.now());
        order.setMessage(message);

        wishRepository.deleteByMemberIdAndProductOptionId(member.getId(), optionId);

        return orderRepository.save(order);
    }
}
