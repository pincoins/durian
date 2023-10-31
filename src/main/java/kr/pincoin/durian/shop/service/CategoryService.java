package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.CategoryCreateRequest;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.CategoryTreePath;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.repository.jpa.CategoryRepository;
import kr.pincoin.durian.shop.repository.jpa.CategoryTreePathRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryTreePathRepository categoryTreePathRepository;

    public List<Category>
    listCategories() {
        return categoryRepository.findCategories();
    }

    public Optional<Category>
    getCategory(Long categoryId, CategoryStatus status) {
        return categoryRepository.findCategory(categoryId, status);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    createRootCategory(CategoryCreateRequest request) {
        Category rootCategory = Category.builder(request)
                .isRoot(true)
                .build();

        categoryRepository.save(rootCategory);
        categoryTreePathRepository.save(rootCategory);

        return Optional.of(rootCategory);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    addChildCategory(Long parentId, CategoryCreateRequest request) {
        Category category = Category.builder(request)
                .isRoot(false)
                .build();

        Category parent = categoryRepository
                .findById(parentId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Parent category not found",
                                                    List.of("Parent category does not exist to add.")));

        List<CategoryTreePath> paths = categoryTreePathRepository.findParentAncestors(parent)
                .stream()
                .map(path -> CategoryTreePath.builder(path.getAncestor(),
                                                      category,
                                                      path.getPathLength() + 1)
                        .build())
                .toList();

        categoryRepository.save(category);
        categoryTreePathRepository.saveAll(paths);
        categoryTreePathRepository.save(category);

        return Optional.of(category);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    hideCategory(Long categoryId) {
        Category category = categoryRepository
                .findCategory(categoryId, CategoryStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Category does not exist to hide.")));
        category.hide();

        return Optional.of(category);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    showCategory(Long categoryId) {
        Category category = categoryRepository
                .findCategory(categoryId, CategoryStatus.HIDDEN)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Category does not exist to show.")));

        category.show();

        return Optional.of(category);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public boolean
    deleteCategory(Long categoryId) {
        return categoryRepository.findCategory(categoryId, CategoryStatus.HIDDEN)
                .map(category -> {
                    categoryRepository.delete(category);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Category not found",
                                                      List.of("Hidden category does not exist to delete.")));
    }
}
