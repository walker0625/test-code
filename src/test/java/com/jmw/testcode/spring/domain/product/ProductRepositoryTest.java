package com.jmw.testcode.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.jmw.testcode.spring.domain.product.ProductSellingStatus.*;
import static com.jmw.testcode.spring.domain.product.ProductType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
        Product p1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product p2 = createProduct("002", HANDMADE, HOLD, "카페라테", 4500);
        Product p3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

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
        Product p1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product p2 = createProduct("002", HANDMADE, HOLD, "카페라테", 4500);
        Product p3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);

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

    @DisplayName("가장 마지막에 저장한 상품의 상품 번호를 가져온다")
    @Test
    void findLatestProductNumber() {

        // given

        String targetProductNumber = "003";

        Product p1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product p2 = createProduct("002", HANDMADE, HOLD, "카페라테", 4500);
        Product p3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 7000);

        productRepository.saveAll(List.of(p1, p2, p3));

        // when
        String productNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(productNumber).isEqualTo(targetProductNumber);
    }

    @DisplayName("가장 마지막에 저장한 상품의 상품 번호를 가져올 때, 상품이 하나도 없는 경우는 null을 반환한다")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {

        // given
        String targetProductNumber = null;

        // when
        String productNumber = productRepository.findLatestProductNumber();

        // then
        assertThat(productNumber).isEqualTo(null);
    }

    private static Product createProduct(String productNumber, ProductType productType, ProductSellingStatus sellingStatus, String name, int price) {
        Product p1 = Product.builder()
                .productNumber(productNumber)
                .type(productType)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
        return p1;
    }

}