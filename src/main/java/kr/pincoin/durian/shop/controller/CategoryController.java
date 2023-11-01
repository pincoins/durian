package kr.pincoin.durian.shop.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.CategoryCreateRequest;
import kr.pincoin.durian.shop.controller.dto.CategoryResponse;
import kr.pincoin.durian.shop.controller.dto.CategoryUpdateRequest;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<CategoryResponse>>
    categoryList(@RequestParam(name = "isRoot", required = false) Boolean isRoot,
                 @RequestParam(name = "status", required = false) CategoryStatus status) {
        return ResponseEntity.ok()
                .body(categoryService.listCategories(isRoot, status)
                              .stream()
                              .map(CategoryResponse::new)
                              .toList());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse>
    categoryDetail(@PathVariable Long categoryId,
                   @RequestParam(name = "status", required = false) CategoryStatus status) {
        return categoryService.getCategory(categoryId, status)
                .map(category -> ResponseEntity.ok().body(new CategoryResponse(category)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Category does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<CategoryResponse>
    categoryCreate(@Valid @RequestBody CategoryCreateRequest request) {
        return categoryService.createRootCategory(request)
                .map(category -> ResponseEntity.ok().body(new CategoryResponse(category)))
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT,
                                                    "Root category creation failure",
                                                    List.of("Failed to create a root category")));
    }

    @PostMapping("{categoryId}/add")
    public ResponseEntity<CategoryResponse>
    categoryAdd(@PathVariable Long categoryId,
                @Valid @RequestBody CategoryCreateRequest request) {
        return categoryService.addChildCategory(categoryId, request)
                .map(category -> ResponseEntity.ok().body(new CategoryResponse(category)))
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT,
                                                    "Child category creation failure",
                                                    List.of("Failed to add a child category")));
    }

    @PutMapping("{categoryId}/update")
    public ResponseEntity<CategoryResponse>
    categoryUpdate(@PathVariable Long categoryId,
                 @Valid @RequestBody CategoryUpdateRequest request) {
        return categoryService.updateCategory(categoryId, request)
                .map(category -> ResponseEntity.ok().body(
                        new CategoryResponse(category)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Failed to update category.")));
    }

    @PutMapping("{categoryId}/hide")
    public ResponseEntity<CategoryResponse>
    categoryHide(@PathVariable Long categoryId) {
        return categoryService.hideCategory(categoryId)
                .map(category -> ResponseEntity.ok().body(
                        new CategoryResponse(category)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Failed to hide category.")));
    }

    @PutMapping("{categoryId}/show")
    public ResponseEntity<CategoryResponse>
    categoryShow(@PathVariable Long categoryId) {
        return categoryService.showCategory(categoryId)
                .map(category -> ResponseEntity.ok().body(
                        new CategoryResponse(category)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Category not found",
                                                    List.of("Failed to show category.")));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object>
    categoryDelete(@PathVariable Long userId) {
        if (categoryService.deleteCategory(userId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
