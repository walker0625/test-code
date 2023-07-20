package com.jmw.testcode.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다")
    @Test
    void containsStockType() {

        // given
        ProductType nonStockType = ProductType.HANDMADE;
        ProductType stockType = ProductType.BOTTLE;

        // when
        boolean resultFalse = ProductType.containsStockType(nonStockType);
        boolean resultTrue = ProductType.containsStockType(stockType);

        // then
        assertThat(resultTrue).isTrue();
        assertThat(resultFalse).isFalse();
    }

}