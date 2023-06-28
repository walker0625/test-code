package com.jmw.testcode.unit;

import com.jmw.testcode.unit.beverage.Americano;
import com.jmw.testcode.unit.beverage.Latte;
import com.jmw.testcode.unit.order.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CafeKioskTest {

    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBeverageList()).hasSize(1);
    }

    @Test
    void addCount() {
        CafeKiosk cafeKiosk = new CafeKiosk();

        cafeKiosk.add(new Americano(), 2);
        assertThat(cafeKiosk.getBeverageList()).hasSize(2);

        cafeKiosk.clear();

        assertThatThrownBy(() -> cafeKiosk.add(new Americano(), 0))
                                          .isInstanceOf(IllegalArgumentException.class)
                                          .hasMessage("음료는 1잔 이상 주문해야 합니다");
    }

    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBeverageList()).hasSize(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverageList()).hasSize(0);
    }

    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        assertThat(cafeKiosk.getBeverageList()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverageList()).hasSize(0);
    }

    @Test
    void createOrder() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 6, 28, 10, 0));

        assertThat(order.getBeverageList()).hasSize(1);

        assertThatThrownBy(() -> cafeKiosk
                .createOrder(LocalDateTime.of(2023, 6, 28, 9, 59)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}