package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.api.controller.request.OrderCreateRequest;
import com.jmw.testcode.spring.api.service.order.response.OrderResponse;
import com.jmw.testcode.spring.domain.order.Order;
import com.jmw.testcode.spring.domain.order.OrderRepository;
import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredTime) {
        List<String> productNumberList = request.getProductNumberList();
        List<Product> duplicateProducts = getDuplicateProducts(productNumberList);

        Order order = Order.create(duplicateProducts, registeredTime);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    private List<Product> getDuplicateProducts(List<String> productNumberList) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumberList);

        Map<String, Product> productMap = products.stream()
                                            .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumberList.stream()
                                .map(productMap::get).collect(Collectors.toList());
    }

}
