package kr.pincoin.durian.shop.repository.jpa;

import jakarta.persistence.EntityManager;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.CategoryTreePath;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.repository.jpa.dto.CategorySelfParentResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryTreePathRepository categoryTreePathRepository;

    @Autowired
    private EntityManager em;

    @Test
    void createCategory() {
        Category category1 = Category.builder("c1",
                                              "slug",
                                              "description",
                                              "sub description",
                                              BigDecimal.ZERO,
                                              CategoryStatus.NORMAL,
                                              0).build();

        categoryRepository.save(category1);
        em.clear();

        Optional<Category> category1Found = categoryRepository.findById(category1.getId());
        assertThat(category1Found).isPresent();
        assertThat(category1Found).hasValue(category1);
    }

    @Test
    void makeTree() {
        Category root = Category.builder("root",
                                         "root",
                                         "description",
                                         "sub description",
                                         BigDecimal.ZERO,
                                         CategoryStatus.NORMAL,
                                         0).build();

        Category second1 = Category.builder("second1",
                                            "second1",
                                            "description",
                                            "sub description",
                                            BigDecimal.ZERO,
                                            CategoryStatus.NORMAL,
                                            1).build();

        Category second2 = Category.builder("second2",
                                            "second2",
                                            "description",
                                            "sub description",
                                            BigDecimal.ZERO,
                                            CategoryStatus.NORMAL,
                                            2).build();

        Category third1 = Category.builder("third1",
                                           "third1",
                                           "description",
                                           "sub description",
                                           BigDecimal.ZERO,
                                           CategoryStatus.NORMAL,
                                           3).build();

        categoryRepository.save(root);
        categoryTreePathRepository.save(root);

        categoryRepository.save(second1);
        categoryTreePathRepository.save(root, second1);

        categoryRepository.save(second2);
        categoryTreePathRepository.save(root, second2);

        categoryRepository.save(third1);
        categoryTreePathRepository.save(second2, third1);

        List<CategoryTreePath> all = categoryTreePathRepository.findAll();
        assertThat(all.size()).isEqualTo(8);

        List<Category> ancestors = categoryRepository.findAncestorCategories(third1.getId());
        assertThat(ancestors.size()).isEqualTo(3);
        assertThat(ancestors.get(0).getTitle()).isEqualTo("root");

        List<Category> children = categoryRepository.findChildCategories(root.getId());
        assertThat(children.size()).isEqualTo(2);

        Optional<Category> parent = categoryRepository.findParentCategory(third1.getId());
        assertThat(parent).isPresent();
        assertThat(parent).hasValue(second2);

        List<CategorySelfParentResult> subTree = categoryRepository.findSubTree(root.getId());
        assertThat(subTree.size()).isEqualTo(3);

        List<Category> leaves = categoryRepository.findLeafCategories(root.getId());
        assertThat(leaves.size()).isEqualTo(2);
    }
}