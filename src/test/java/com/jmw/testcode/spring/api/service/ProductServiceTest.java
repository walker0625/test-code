package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductRepository;
import com.jmw.testcode.spring.domain.product.ProductSellingStatus;
import com.jmw.testcode.spring.domain.product.ProductType;
import com.jmw.testcode.spring.domain.product.request.ProductCreateRequest;
import com.jmw.testcode.spring.domain.product.response.ProductResponse;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.jmw.testcode.spring.domain.product.ProductSellingStatus.SELLING;
import static com.jmw.testcode.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.*;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록한다, 상품번호는 가장 최근 등록된 상품번호에서 1 증가한 값이다")
    @Test
    void createProduct() {

        // given
        Product p1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        productRepository.save(p1);

        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse).extracting("productNumber", "type", "sellingStatus", "name", "price")
                                   .contains("002", HANDMADE, SELLING, "카푸치노", 5000);

        List<Product> productList = productRepository.findAll();

        assertThat(productList).hasSize(2).extracting("productNumber", "type", "sellingStatus", "name", "price")
                                        .containsExactlyInAnyOrder(
                                                tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
                                                tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
                                        );
    }

    @DisplayName("신규 상품을 등록한다, 상품이 하나도 없는 경우는 상품번호는 001이 된다")
    @Test
    void createFirstProduct() {

        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        assertThat(productResponse).extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains("001", HANDMADE, SELLING, "카푸치노", 5000);

        List<Product> productList = productRepository.findAll();

        assertThat(productList).hasSize(1).extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", HANDMADE, SELLING, "카푸치노", 5000)
                );
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