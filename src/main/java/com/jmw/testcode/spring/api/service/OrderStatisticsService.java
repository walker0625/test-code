package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.api.service.mail.MailService;
import com.jmw.testcode.spring.domain.order.Order;
import com.jmw.testcode.spring.domain.order.OrderRepository;
import com.jmw.testcode.spring.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final MailService mailService;
    private final OrderRepository orderRepository;

    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {
        List<Order> orderList = orderRepository.findOrdersBy(orderDate.atStartOfDay(),
                                                             orderDate.plusDays(1).atStartOfDay(),
                                                             OrderStatus.PAYMENT_COMPLETED);

        int totalAmount = orderList.stream().mapToInt(Order::getTotalPrice).sum();

        boolean result  = mailService.sendMail("no-reply@test.com",
                                               email,
                                               String.format("[매출통계]8월 결산 %s", orderDate),
                                               String.format("총 매출 합계는 %s원입니다.", totalAmount)
                                               );

        if (!result) {
            throw new IllegalArgumentException("메일 전송에 실패했습니다");
        }

        return true;
    }

}
