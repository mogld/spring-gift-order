package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.OrderRequest;
import gift.model.Member;
import gift.model.Order;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
        Order order = orderService.createOrder(orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage(), member);
    }
}