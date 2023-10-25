package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.dto.CategoryCreateRequest;
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
    createCategory(CategoryCreateRequest request) {
        Category category = categoryRepository.save(new Category(request));
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
        categoryRepository.save(category);

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
        categoryRepository.save(category);

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
