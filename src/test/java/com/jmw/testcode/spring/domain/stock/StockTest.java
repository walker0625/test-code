package com.jmw.testcode.spring.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    @DisplayName("재고의 수량이 주문하려는 수량보다 작은지 확인한다")
    @Test
    void test1() {

        // given
        Stock stock = Stock.create("001", 1);
        int q = 2;

        // when
        boolean result = stock.isQuantityLessThan(q);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("재고에서 주문수량 만큼을 차감한다")
    @Test
    void test2() {

        // given
        Stock stock = Stock.create("001", 1);
        int q = 1;

        // when
        stock.deductQuantity(q);

        // then
        assertThat(stock.getQuantity()).isZero();
    }

    @DisplayName("재고보다 많은 수량을 차감할 경우 예외가 발생한다")
    @Test
    void test3() {

        // given
        Stock stock = Stock.create("001", 1);
        int q = 2;

        // when // then
        assertThatThrownBy(() -> stock.deductQuantity(q))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("need more stock");
    }

}