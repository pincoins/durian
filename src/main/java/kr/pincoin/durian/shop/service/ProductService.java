package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.ProductChangePriceRequest;
import kr.pincoin.durian.shop.controller.dto.ProductChangeStockLevelRequest;
import kr.pincoin.durian.shop.controller.dto.ProductCreateRequest;
import kr.pincoin.durian.shop.controller.dto.ProductUpdateRequest;
import kr.pincoin.durian.shop.domain.Category;
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
                 String categorySlug,
                 String slug,
                 Long[] products,
                 ProductStatus status,
                 ProductStockStatus stock,
                 Boolean removed) {
        return productRepository.findProducts(categoryId, categorySlug, slug, products, status, stock, removed);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findCategory(request.getCategoryId(),
                                                            CategoryStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                                                    "Invalid category",
                                                    List.of("Normal category does not exist.")));

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
                                          0).build();

        category.add(product);
        productRepository.save(product);

        return Optional.of(product);
    }

    public Optional<Product>
    getProduct(Long productId,
               String slug,
               ProductStatus status,
               ProductStockStatus stock,
               Boolean removed) {
        return productRepository.findProduct(productId, null, slug, status, stock, removed);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findProduct(productId,
                                                        null,
                                                        null,
                                                        ProductStatus.ENABLED,
                                                        ProductStockStatus.IN_STOCK,
                                                        false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to update.")));

        return Optional.of(product.update(request));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    disableProduct(Long productId) {
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             ProductStatus.ENABLED,
                             null,
                             null)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to disable.")));

        return Optional.of(product.disable());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    enableProduct(Long productId) {
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             ProductStatus.DISABLED,
                             null,
                             null)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to enable.")));

        return Optional.of(product.enable());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    fillProduct(Long productId) {
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             ProductStatus.ENABLED,
                             ProductStockStatus.SOLD_OUT,
                             null)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to fill up stock.")));

        return Optional.of(product.fill());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    makeEmptyProduct(Long productId) {
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             ProductStatus.ENABLED,
                             ProductStockStatus.IN_STOCK,
                             null)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to be sold out.")));

        return Optional.of(product.makeEmpty());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    removeProduct(Long productId) {
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             null,
                             null,
                             false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to remove.")));

        return Optional.of(product.remove());

    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    restoreProduct(Long productId) {
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             null,
                             null,
                             true)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to restore.")));

        return Optional.of(product.restore());
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    changeCategory(Long productId, Long categoryId) {
        Category category = categoryRepository
                .findCategory(categoryId, CategoryStatus.NORMAL)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Normal category not found",
                                                    List.of("Category does not exist for product to change.")));
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             null,
                             null,
                             false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to change category.")));

        return Optional.of(product.belongsTo(category));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    changeProductPrice(Long productId, ProductChangePriceRequest request) {
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             ProductStatus.ENABLED,
                             ProductStockStatus.IN_STOCK,
                             false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to change price.")));

        return Optional.of(product.changePrice(request));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public Optional<Product>
    changeProductStockLevel(Long productId, ProductChangeStockLevelRequest request) {
        Product product = productRepository
                .findProduct(productId,
                             null,
                             null,
                             ProductStatus.ENABLED,
                             ProductStockStatus.IN_STOCK,
                             false)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to change stock level.")));

        return Optional.of(product.changeStockLevel(request));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('SYSADMIN', 'STAFF')")
    public boolean deleteProduct(Long productId) {
        return productRepository.findProduct(productId,
                                             null,
                                             null,
                                             null,
                                             null,
                                             true)
                .map(product -> {
                    productRepository.delete(product);
                    return true;
                }).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                      "Soft removed product not found",
                                                      List.of("Product does not exist to delete.")));
    }
}
