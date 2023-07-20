package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.api.controller.request.OrderCreateRequest;
import com.jmw.testcode.spring.api.service.order.response.OrderResponse;
import com.jmw.testcode.spring.domain.order.OrderRepository;
import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductRepository;
import com.jmw.testcode.spring.domain.product.ProductType;
import com.jmw.testcode.spring.domain.stock.Stock;
import com.jmw.testcode.spring.domain.stock.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.jmw.testcode.spring.domain.product.ProductSellingStatus.SELLING;
import static com.jmw.testcode.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private StockRepository stockRepository;

    /*
    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
    }
    */

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

    @DisplayName("중복되는 주문번호로 주문을 생성 할 수 있다")
    @Test
    void createOrderDuplicate() {

        // given
        Product p1 = createProduct(HANDMADE, "001", 4000);
        Product p2 = createProduct(HANDMADE, "002", 4500);
        Product p3 = createProduct(HANDMADE, "003", 7000);
        productRepository.saveAll(List.of(p1, p2, p3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                                                        .productNumberList(List.of("001", "001"))
                                                        .build();

        // when
        LocalDateTime now = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request, now);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse).extracting("registeredDateTime", "totalPrice")
                                 .contains(now, 8000);
        assertThat(orderResponse.getProductResponseList()).hasSize(2)
                                                          .extracting("productNumber", "price")
                                                          .containsExactlyInAnyOrder(
                                                                  tuple("001", 4000),
                                                                  tuple("001", 4000)
                                                          );
    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다")
    @Test
    void createOrderWithStock() {

        // given
        Product p1 = createProduct(BOTTLE, "001", 4000);
        Product p2 = createProduct(BAKERY, "002", 4500);
        Product p3 = createProduct(HANDMADE, "003", 7000);
        productRepository.saveAll(List.of(p1, p2, p3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumberList(List.of("001", "001","002", "003"))
                .build();

        // when
        LocalDateTime now = LocalDateTime.now();
        OrderResponse orderResponse = orderService.createOrder(request, now);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse).extracting("registeredDateTime", "totalPrice")
                                    .contains(now, 19500);
        assertThat(orderResponse.getProductResponseList()).hasSize(4)
                                                            .extracting("productNumber", "price")
                                                            .containsExactlyInAnyOrder(
                                                                    tuple("001", 4000),
                                                                    tuple("001", 4000),
                                                                    tuple("002", 4500),
                                                                    tuple("003", 7000)
                                                            );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
                            .extracting("productNumber", "quantity")
                            .containsExactlyInAnyOrder(
                                    tuple("001", 0),
                                    tuple("002", 1)
                            );

    }


    @DisplayName("재고가 없는 상품을 주문할 경우 예외가 발생한다")
    @Test
    void createOrderWithOutStock() {

        // given
        Product p1 = createProduct(BOTTLE, "001", 4000);
        Product p2 = createProduct(BAKERY, "002", 4500);
        Product p3 = createProduct(HANDMADE, "003", 7000);
        productRepository.saveAll(List.of(p1, p2, p3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stock1.deductQuantity(1);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumberList(List.of("001", "001", "002"))
                .build();

        // when
        LocalDateTime now = LocalDateTime.now();

        // then
        assertThatThrownBy(() -> orderService.createOrder(request, now)).isInstanceOf(IllegalArgumentException.class).hasMessage("재고가 부족한 상품이 있습니다");
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