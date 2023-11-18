package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.CategoryCreateRequest;
import kr.pincoin.durian.shop.controller.dto.CategoryUpdateRequest;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.repository.jpa.CategoryRepository;
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

    public List<Category> listCategories(CategoryStatus status, String slug) {
        return categoryRepository.findCategories(status, slug);
    }

    public Optional<Category>
    getCategory(Long categoryId, CategoryStatus status) {
        return categoryRepository.findCategory(categoryId, status, null);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    createRootCategory(CategoryCreateRequest request) {
        Category category = Category.builder(request)
                .build();

        preventDuplicateSlug(category);

        categoryRepository.save(category);
        // make flat category
        // categoryTreePathRepository.save(rootCategory);

        return Optional.of(category);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    updateCategory(Long categoryId, CategoryUpdateRequest request) {
        Category category = categoryRepository
                .findCategory(categoryId, CategoryStatus.NORMAL, null)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Category does not exist to update.")));
        return Optional.of(category.update(request));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    hideCategory(Long categoryId) {
        Category category = categoryRepository
                .findCategory(categoryId, CategoryStatus.NORMAL, null)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Category does not exist to hide.")));
        return Optional.of(category.hide());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    showCategory(Long categoryId) {
        Category category = categoryRepository
                .findCategory(categoryId, CategoryStatus.HIDDEN, null)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Category does not exist to show.")));
        return Optional.of(category.show());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    removeCategory(Long categoryId) {
        Category category = categoryRepository
                .findCategory(categoryId, CategoryStatus.HIDDEN, null)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Category does not exist to remove.")));

        return Optional.of(category.remove());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Category>
    restoreCategory(Long categoryId) {
        Category category = categoryRepository
                .findCategory(categoryId, CategoryStatus.HIDDEN, null)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Category does not exist to restore.")));

        return Optional.of(category.restore());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public boolean
    deleteCategory(Long categoryId) {
        return categoryRepository.findCategory(categoryId, CategoryStatus.HIDDEN, null)
                .map(category -> {
                    categoryRepository.delete(category);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Category not found",
                                                      List.of("Hidden category does not exist to delete.")));
    }

    private void
    preventDuplicateSlug(Category category) {
        List<Category> categories = categoryRepository.findCategories(null, category.getSlug());
        if (!categories.isEmpty()) {
            throw new ApiException(HttpStatus.CONFLICT,
                                   "Duplicate category slug",
                                   List.of(String.format("The slug '%s' is already exists.", category.getSlug())));
        }
    }
}
