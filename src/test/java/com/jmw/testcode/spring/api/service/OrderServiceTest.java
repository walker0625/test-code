package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.api.controller.request.OrderCreateRequest;
import com.jmw.testcode.spring.api.service.order.response.OrderResponse;
import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductRepository;
import com.jmw.testcode.spring.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.jmw.testcode.spring.domain.product.ProductSellingStatus.*;
import static com.jmw.testcode.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {

        // given
        Product p1 = createProduct(HANDMADE, "001", 4000);
        Product p2 = createProduct(HANDMADE, "002", 4500);
        Product p3 = createProduct(HANDMADE, "003", 7000);

        productRepository.saveAll(List.of(p1, p2, p3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumberList(List.of("001", "002"))
                .build();

        // when
        LocalDateTime now = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request, now);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse).extracting("registeredDateTime", "totalPrice")
                .contains(now, 8500);
        assertThat(orderResponse.getProductResponseList()).hasSize(2)
                                                          .extracting("productNumber", "price")
                                                          .containsExactlyInAnyOrder(
                                                                  tuple("001", 4000),
                                                                  tuple("002", 4500)
                                                          );

    }

    private Product createProduct(ProductType productType, String productNumber, int price) {
        return Product.builder()
                        .type(productType)
                        .productNumber(productNumber)
                        .price(price)
                        .sellingStatus(SELLING)
                        .name("메뉴 이름")
                        .build();
    }

}