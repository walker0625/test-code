package com.jmw.testcode.unit;

import com.jmw.testcode.unit.beverage.Americano;
import com.jmw.testcode.unit.beverage.Latte;
import com.jmw.testcode.unit.order.Order;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

// 테스트는 문서다
class CafeKioskTest {

    @DisplayName("음료 1잔을 추가하면 목록에 담긴다") // 메소드명보다 우선시되며 문장 형태로 작성하는 것이 좋다
    @Test
    void 음료_한잔_추가() {
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


    @DisplayName("영업시작 시간 이전에는 주문을 생성 할 수 없다") // 도메인 용어(영업시간)을 사용하며 좀 더 자세한 정보를 제공
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

    // BDD 방식(given>when>then)으로 작성하면 displayName을 작성하기 더 손쉬움
    @DisplayName("주문 목록에 담긴 상품들의 총 금액을 계산할 수 있다")
    @Test
    void calculateTotalPrice() {
        // given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        // when
        int totalPrice = cafeKiosk.calculateTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(9000);
    }

}