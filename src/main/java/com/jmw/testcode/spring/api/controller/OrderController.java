package com.jmw.testcode.spring.api.controller;

import com.jmw.testcode.spring.api.controller.request.OrderCreateRequest;
import com.jmw.testcode.spring.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public void createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime registeredTime = LocalDateTime.now(); // test하기 어려운 정보를 컨트롤 하기 위해 파라미터화
        orderService.createOrder(request, registeredTime);
    }

}
