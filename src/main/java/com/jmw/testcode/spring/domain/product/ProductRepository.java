package com.jmw.testcode.spring.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * SELECT *
     * FROM
     * WHERE selling_type IN ("SELLING", "HOLD")
     */
    List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

    List<Product> findAllByProductNumberIn(List<String> productNumberList);

    @Query(value = "SELECT product_number FROM product ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLatestProductNumber();

}
