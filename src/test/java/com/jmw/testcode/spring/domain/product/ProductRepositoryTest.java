package com.jmw.testcode.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.jmw.testcode.spring.domain.product.ProductSellingStatus.*;
import static com.jmw.testcode.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
//@SpringBootTest 상대적으로 무거우나 전체적인 흐름을 위해서는 권장
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다")
    @Test
    void findProduct() {

        // given
        Product p1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product p2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라테")
                .price(4500)
                .build();

        Product p3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(p1, p2, p3));


        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));


        // then
        assertThat(products).hasSize(2)
                            .extracting("productNumber", "name", "sellingStatus")
                            .containsExactlyInAnyOrder(
                                    tuple("001", "아메리카노", SELLING),
                                    tuple("002", "카페라테", HOLD)
                            );

    }

    @DisplayName("상품번호로 상품들을 조회한다")
    @Test
    void findProductByProductNumbers() {

        // given
        Product p1 = Product.builder()
                .productNumber("001")
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        Product p2 = Product.builder()
                .productNumber("002")
                .type(HANDMADE)
                .sellingStatus(HOLD)
                .name("카페라테")
                .price(4500)
                .build();

        Product p3 = Product.builder()
                .productNumber("003")
                .type(HANDMADE)
                .sellingStatus(STOP_SELLING)
                .name("팥빙수")
                .price(7000)
                .build();

        productRepository.saveAll(List.of(p1, p2, p3));


        // when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));


        // then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라테", HOLD)
                );

    }

}