package com.jmw.testcode.spring.domain.product.request;

import com.jmw.testcode.spring.domain.product.Product;
import com.jmw.testcode.spring.domain.product.ProductSellingStatus;
import com.jmw.testcode.spring.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다")
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다")
    private ProductSellingStatus sellingStatus;

    @NotBlank(message = "상품 이름은 필수입니다") // @NotNull @NotEmpty("  " 불가) 둘 포함
    private String name;

    @Positive(message = "상품 가격은 양수여야 합니다")
    private int price;

    @Builder
    public ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public Product toEntity(String nextProductNumber) {
        return Product.builder()
                .productNumber(nextProductNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();

    }
}
