package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.api.controller.request.OrderCreateRequest;
import com.jmw.testcode.spring.api.service.order.request.OrderCreateServiceRequest;
import com.jmw.testcode.spring.api.service.order.response.OrderResponse;
import com.jmw.testcode.spring.domain.order.Order;
import com.jmw.testcode.spring.domain.order.OrderRepository;
import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductRepository;
import com.jmw.testcode.spring.domain.product.ProductType;
import com.jmw.testcode.spring.domain.stock.Stock;
import com.jmw.testcode.spring.domain.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public OrderResponse createOrder(OrderCreateServiceRequest request, LocalDateTime registeredTime) {
        List<String> productNumberList = request.getProductNumberList();
        List<Product> duplicateProducts = getDuplicateProducts(productNumberList);

        deductStockQuantity(duplicateProducts);

        Order order = Order.create(duplicateProducts, registeredTime);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.of(savedOrder);
    }

    private void deductStockQuantity(List<Product> duplicateProducts) {
        // 재고 차감 체크가 필요한 상품들 filter
        List<String> stockList = duplicateProducts.stream()
                                                .filter(p -> ProductType.containsStockType(p.getType()))
                                                .map(Product::getProductNumber)
                                                .collect(Collectors.toList());

        // 재고 엔티티 조회
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockList);
        Map<String, Stock> stockMap = stocks.stream().collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));


        // 상품별 counting
        Map<String, Long> productCountingMap = stockList.stream().collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        // 재고 차감 시도
        for (String s : new HashSet<>(stockList)) {
            Stock stock = stockMap.get(s);
            int q = productCountingMap.get(s).intValue();

            if(stock.isQuantityLessThan(q)) {
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다");
            }

            stock.deductQuantity(q);

        }
    }

    private List<Product> getDuplicateProducts(List<String> productNumberList) {
        List<Product> products = productRepository.findAllByProductNumberIn(productNumberList);

        Map<String, Product> productMap = products.stream()
                                                  .collect(Collectors.toMap(Product::getProductNumber, p -> p));

        return productNumberList.stream()
                                .map(productMap::get).collect(Collectors.toList());
    }

}