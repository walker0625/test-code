package com.jmw.testcode.spring.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmw.testcode.spring.api.controller.request.OrderCreateRequest;
import com.jmw.testcode.spring.api.service.OrderService;
import com.jmw.testcode.spring.api.service.ProductService;
import com.jmw.testcode.spring.domain.product.ProductSellingStatus;
import com.jmw.testcode.spring.domain.product.ProductType;
import com.jmw.testcode.spring.domain.product.request.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean // ProductController가 의존하고 있는 Service를 mock 처리함
    private OrderService orderService;

    @DisplayName("신규 주문을 등록한다")
    @Test
    void createOrder() throws Exception {

        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumberList(List.of("001"))
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @DisplayName("신규 주문을 등록할 때 상품번호는 1개 이상이어야 한다")
    @Test
    void createOrderEmptyNumbers() throws Exception {

        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumberList(List.of())
                .build();

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("상품 번호 리스트는 필수입니다"));
    }

}