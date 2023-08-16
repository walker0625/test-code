package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.client.mail.MailSendClient;
import com.jmw.testcode.spring.domain.history.mail.MailSendHistory;
import com.jmw.testcode.spring.domain.history.mail.MailSendHistoryRepository;
import com.jmw.testcode.spring.domain.order.Order;
import com.jmw.testcode.spring.domain.order.OrderRepository;
import com.jmw.testcode.spring.domain.order.OrderStatus;
import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductRepository;
import com.jmw.testcode.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.jmw.testcode.spring.domain.product.ProductSellingStatus.SELLING;
import static com.jmw.testcode.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @MockBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder().productList(products).orderStatus(OrderStatus.PAYMENT_COMPLETED).registeredDateTime(now).build();
        return orderRepository.save(order);
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다")
    @Test
    void sendMail() {

        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 0, 0);

        // given
        Product p1 = createProduct(HANDMADE, "001", 1000);
        Product p2 = createProduct(HANDMADE, "002", 2000);
        Product p3 = createProduct(HANDMADE, "003", 5000);

        List<Product> products = List.of(p1, p2, p3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 8, 15, 23, 59, 59), products);
        Order order2 = createPaymentCompletedOrder(now, products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 8, 16, 23, 59,59), products);
        Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 8, 17, 0, 0), products);

        // mock 객체의 행위를 지정(stubbing) - 실제 메일전송을 하는 것을 대체
        Mockito.when(mailSendClient.sendEmail(any(String.class), any(String.class), any(String.class), any(String.class)))
               .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2023, 8, 16), "test@gmail.com");

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 16000원입니다.");
    }

    private static Product createProduct(ProductType productType, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .price(price)
                .build();
    }

}