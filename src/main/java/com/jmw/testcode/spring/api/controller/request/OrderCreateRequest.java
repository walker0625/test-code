package com.jmw.testcode.spring.api.controller.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    private List<String> productNumberList;

    @Builder
    public OrderCreateRequest(List<String> productNumberList) {
        this.productNumberList = productNumberList;
    }

}
