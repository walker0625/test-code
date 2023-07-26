package com.jmw.testcode.spring.api.service.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest { // validation 책임은 OrderCreateRequest가 가짐(Service 모듈만 분리할 경우에 용이)

    private List<String> productNumberList;

    @Builder
    public OrderCreateServiceRequest(List<String> productNumberList) {
        this.productNumberList = productNumberList;
    }

}
