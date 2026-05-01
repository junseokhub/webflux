package com.mvc.load.product;

import com.mvc.load.common.exception.BusinessException;
import com.mvc.load.common.exception.ErrorCode;
import com.mvc.load.user.User;
import com.mvc.load.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;

    @Transactional
    public Product createProduct(Long userId, String name, Integer price, Integer stock) {
        User user = userService.findById(userId);
        Product product = Product.builder()
                .name(name)
                .price(price)
                .stock(stock)
                .createdBy(user)
                .build();

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }


    @Transactional
    public Product findByIdWithLock(Long id) {
        return productRepository.findByIdWithLock(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

}
