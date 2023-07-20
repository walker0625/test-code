package com.jmw.testcode.spring.domain.stock;

import com.jmw.testcode.spring.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    private int quantity;

    @Builder
    public Stock(String productNumber, int quantity) {
        this.productNumber = productNumber;
        this.quantity = quantity;
    }

    public static Stock create(String productNumber, int quantity) {
        return Stock.builder().productNumber(productNumber).quantity(quantity).build();
    }

    public boolean isQuantityLessThan(int q) {
        return this.quantity < q;
    }

    public void deductQuantity(int q) {
        if(isQuantityLessThan(q)) {
            throw new IllegalArgumentException("need more stock");
        }
        this.quantity -= q;
    }

}
