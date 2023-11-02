package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.ProductCreateRequest;
import kr.pincoin.durian.shop.domain.Price;
import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.StockLevel;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import kr.pincoin.durian.shop.domain.conveter.ProductStockStatus;
import kr.pincoin.durian.shop.repository.jpa.CategoryRepository;
import kr.pincoin.durian.shop.repository.jpa.ProductRepository;
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
public class ProductService {
    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public List<Product>
    listProducts(Long categoryId,
                 String slug,
                 ProductStatus status,
                 ProductStockStatus stock,
                 Boolean removed) {
        return productRepository.findProducts(categoryId, slug, status, stock, removed);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    createProduct(ProductCreateRequest request) {
        return categoryRepository.findCategory(request.getCategoryId(), null, CategoryStatus.NORMAL, null)
                .map(category -> {
                    Product product = Product.builder(request.getSlug(),
                                                      request.getName(),
                                                      request.getSubtitle(),
                                                      request.getDescription(),
                                                      0,
                                                      new Price(request.getListPrice(),
                                                                request.getSellingPrice(),
                                                                request.getBuyingPrice()),
                                                      new StockLevel(request.getMinimumStockLevel(),
                                                                     request.getMaximumStockLevel()),
                                                      category).build();
                    productRepository.save(product);

                    return Optional.of(product);
                })
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Invalid category",
                                                    List.of("Normal category does not exist.")));
    }
}
