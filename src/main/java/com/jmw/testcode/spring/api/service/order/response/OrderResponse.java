package com.jmw.testcode.spring.api.service.order.response;

import com.jmw.testcode.spring.domain.order.Order;
import com.jmw.testcode.spring.domain.product.response.ProductResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    private List<ProductResponse> productResponseList;

    @Builder
    public OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> productResponseList) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.productResponseList = productResponseList;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .productResponseList(order.getOrderProductList().stream().map(orderProduct -> ProductResponse.of(orderProduct.getProduct())).collect(Collectors.toList()))
                .build();
    }
}
