package com.jmw.testcode.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    /**
     * SELECT *
     * FROM
     * WHERE selling_type IN ("SELLING", "HOLD")
     *
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

}
