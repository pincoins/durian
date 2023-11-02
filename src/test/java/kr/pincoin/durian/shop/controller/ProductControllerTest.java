package kr.pincoin.durian.shop.controller;

import kr.pincoin.durian.shop.controller.dto.ProductAdminResponse;
import kr.pincoin.durian.shop.controller.dto.ProductResponse;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.Price;
import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.StockLevel;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.repository.jpa.CategoryRepository;
import kr.pincoin.durian.shop.repository.jpa.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class ProductControllerTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void dtoTest() {
        Category category = Category.builder("root",
                                             "root",
                                             "description",
                                             "sub description",
                                             BigDecimal.ZERO,
                                             CategoryStatus.NORMAL)
                .build();

        categoryRepository.save(category);

        Product product = Product.builder("slug",
                                          "name",
                                          "subtitle",
                                          "description",
                                          0,
                                          new Price(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE),
                                          new StockLevel(0, 0),
                                          category)
                .build();

        productRepository.save(product);

        ProductResponse productResponse = new ProductResponse(product);
        assertThat(productResponse.getName()).isEqualTo("name");

        ProductResponse productAdminResponse = new ProductAdminResponse(product);
        assertThat(productAdminResponse.getName()).isEqualTo("name");
    }
}