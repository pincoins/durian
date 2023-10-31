package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
@Slf4j
class CategoryTreePathRepositoryTest {
    @Autowired
    private CategoryTreePathRepository categoryTreePathRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void jpqlInsertTest() {
        Category root = Category.builder("root",
                                         "slug",
                                         "description",
                                         "sub description",
                                         BigDecimal.ZERO,
                                         CategoryStatus.NORMAL).build();
        categoryRepository.save(root);
        categoryTreePathRepository.save(root);
    }
}