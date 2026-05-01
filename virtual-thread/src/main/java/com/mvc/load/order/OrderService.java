package com.mvc.load.order;

import com.mvc.load.common.exception.BusinessException;
import com.mvc.load.common.exception.ErrorCode;
import com.mvc.load.product.Product;
import com.mvc.load.product.ProductService;
import com.mvc.load.user.User;
import com.mvc.load.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;

    @Transactional
    public Order createOrder(Long userId, Long productId) {
        User user = userService.findById(userId);

        Product product = productService.findByIdWithLock(productId);

        if (product.getStock() <= 0) {
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
        }

        product.decrementStock();
        return orderRepository.save(Order.builder()
                .user(user)
                .product(product)
                .originalPrice(product.getPrice())
                .finalPrice(product.getPrice())
                .build());

    }


}
