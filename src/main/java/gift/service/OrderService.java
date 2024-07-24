
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private WishService wishService;

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

        return orderRepository.save(order);
    }
}
