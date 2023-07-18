package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.api.controller.request.OrderCreateRequest;
import com.jmw.testcode.spring.api.service.order.response.OrderResponse;
import com.jmw.testcode.spring.domain.order.Order;
import com.jmw.testcode.spring.domain.order.OrderRepository;
import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredTime) {
        List<String> productNumberList = request.getProductNumberList();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumberList);

        Order order = Order.create(products, registeredTime);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

}
