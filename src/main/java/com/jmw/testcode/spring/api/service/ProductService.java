package com.jmw.testcode.spring.api.service;

import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductRepository;
import com.jmw.testcode.spring.domain.product.ProductSellingStatus;
import com.jmw.testcode.spring.domain.product.request.ProductCreateRequest;
import com.jmw.testcode.spring.domain.product.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * readonly = true : 읽기 전용 > cud(command) 동작 x / only read 만 동작 > read 메소드에 불필요한 트랜잭션 동작을 제거 가능
 * jpa : cud 스냅샷 저장, 변경감지 x(성능 향상)
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());
        return products.stream()
                .map(product -> ProductResponse.of(product))
                .collect(Collectors.toList());
    }

    @Transactional // 기본 값이 readOnly = false (CUD 작업만 추가)
    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();

        if(latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }

}
