package com.jmw.testcode.spring.domain.order;

import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.jmw.testcode.spring.domain.product.ProductSellingStatus.SELLING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @DisplayName("주문 생성시에 상품 리스트에서 주문의 총 금액을 계산한다")
    @Test
    void calculateTotalPrice() {

        // given
        List<Product> products = List.of(createProduct("001", 1000),
                createProduct("002", 2000));

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성시에 상품 상태는 INIT이다")
    @Test
    void init() {

        // given
        List<Product> products = List.of(createProduct("001", 1000),
                createProduct("002", 2000));

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성시에 주문 등록 시간을 기록한다")
    @Test
    void registerdDateTime() {

        // given
        LocalDateTime time = LocalDateTime.now();
        List<Product> products = List.of(createProduct("001", 1000),
                                 createProduct("002", 2000));

        // when
        Order order = Order.create(products, time);

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(time);
    }

    private Product createProduct(String productNumber, int price) {
        return Product.builder()
                .type(ProductType.HANDMADE)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .build();
    }

}