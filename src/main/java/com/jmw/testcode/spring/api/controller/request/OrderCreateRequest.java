package com.jmw.testcode.spring.api.controller.request;

import com.jmw.testcode.spring.api.service.order.request.OrderCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotEmpty(message = "상품 번호 리스트는 필수입니다")
    private List<String> productNumberList;

    @Builder
    public OrderCreateRequest(List<String> productNumberList) {
        this.productNumberList = productNumberList;
    }

    public OrderCreateServiceRequest toServiceRequest() {
        return OrderCreateServiceRequest.builder()
                                        .productNumberList(productNumberList)
                                        .build();
    }

}
