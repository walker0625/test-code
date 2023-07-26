package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.domain.orderproduct.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
