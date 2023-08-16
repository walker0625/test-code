package com.jmw.testcode.spring.domain.order;

import com.jmw.testcode.spring.domain.BaseEntity;
import com.jmw.testcode.spring.domain.orderproduct.OrderProduct;
import com.jmw.testcode.spring.domain.product.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;

    private LocalDateTime registeredDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProductList = new ArrayList<>();

    @Builder
    private Order(List<Product> productList, OrderStatus orderStatus, LocalDateTime registeredDateTime) {
        this.orderStatus = orderStatus;
        this.totalPrice = productList.stream().mapToInt(Product::getPrice).sum();
        this.registeredDateTime = registeredDateTime;
        this.orderProductList = productList.stream().map(p -> new OrderProduct(this, p)).collect(Collectors.toList());
    }

    public static Order create(List<Product> products, LocalDateTime registeredDateTime) {
        return Order.builder()
                .orderStatus(OrderStatus.INIT)
                .productList(products)
                .registeredDateTime(registeredDateTime)
                .build();
    }

}
