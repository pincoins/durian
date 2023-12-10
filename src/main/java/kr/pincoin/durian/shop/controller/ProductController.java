package kr.pincoin.durian.shop.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.auth.service.IdentityService;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.*;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import kr.pincoin.durian.shop.domain.conveter.ProductStockStatus;
import kr.pincoin.durian.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    private final IdentityService identityService;

    @GetMapping("")
    public ResponseEntity<List<? extends ProductResponse>>
    productList(@RequestParam(name = "categoryId", required = false) Long categoryId,
                @RequestParam(name = "categorySlug", required = false) String categorySlug,
                @RequestParam(name = "slug", required = false) String slug,
                @RequestParam(name = "status", required = false) ProductStatus status,
                @RequestParam(name = "stock", required = false) ProductStockStatus stock,
                @RequestParam(name = "removed", required = false) Boolean removed,
                @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .ok()
                .body(productService.listProducts(categoryId, categorySlug, slug, status, stock, removed)
                              .stream()
                              .map(product -> identityService.isAdmin(userDetails)
                                      ? new ProductAdminResponse(product)
                                      : new ProductResponse(product))
                              .toList());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse>
    productDetail(@PathVariable Long productId,
                  @RequestParam(name = "slug", required = false) String slug,
                  @RequestParam(name = "status", required = false) ProductStatus status,
                  @RequestParam(name = "stock", required = false) ProductStockStatus stock,
                  @RequestParam(name = "removed", required = false) Boolean removed,
                  @AuthenticationPrincipal UserDetails userDetails) {
        return productService.getProduct(productId, slug, status, stock, removed)
                .map(product -> ResponseEntity
                        .ok()
                        .body(identityService.isAdmin(userDetails)
                                      ? new ProductAdminResponse(product)
                                      : new ProductResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Product does not exist to retrieve.")));
    }

    @PostMapping("")
    public ResponseEntity<ProductAdminResponse>
    productCreate(@Valid @RequestBody ProductCreateRequest request) {
        return productService.createProduct(request)
                .map(product -> ResponseEntity.ok().body(new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT,
                                                    "Product creation failure",
                                                    List.of("Failed to create a new product.")));
    }

    @PutMapping("{productId}")
    public ResponseEntity<ProductAdminResponse>
    productUpdate(@PathVariable Long productId,
                  @Valid @RequestBody ProductUpdateRequest request) {
        return productService.updateProduct(productId, request)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to update product.")));
    }

    @PutMapping("{productId}/disable")
    public ResponseEntity<ProductAdminResponse>
    productDisable(@PathVariable Long productId) {
        return productService.disableProduct(productId)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to disable product.")));
    }

    @PutMapping("{productId}/enable")
    public ResponseEntity<ProductAdminResponse>
    productEnable(@PathVariable Long productId) {
        return productService.enableProduct(productId)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to enable product.")));
    }

    @PutMapping("{productId}/fill")
    public ResponseEntity<ProductAdminResponse>
    productFill(@PathVariable Long productId) {
        return productService.fillProduct(productId)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to fill stock up.")));
    }

    @PutMapping("{productId}/empty")
    public ResponseEntity<ProductAdminResponse>
    productEmpty(@PathVariable Long productId) {
        return productService.makeEmptyProduct(productId)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to be out of stock.")));
    }

    @PutMapping("{productId}/remove")
    public ResponseEntity<ProductAdminResponse>
    productRemove(@PathVariable Long productId) {
        return productService.removeProduct(productId)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to remove product.")));
    }

    @PutMapping("{productId}/restore")
    public ResponseEntity<ProductAdminResponse>
    productRestore(@PathVariable Long productId) {
        return productService.restoreProduct(productId)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to restore product.")));
    }

    @PutMapping("{productId}/categories/{categoryId}")
    public ResponseEntity<ProductAdminResponse>
    productChangeCategory(@PathVariable Long productId,
                          @PathVariable Long categoryId) {
        return productService.changeCategory(productId, categoryId)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to change category of product.")));
    }

    @PutMapping("{productId}/change-price")
    public ResponseEntity<ProductAdminResponse>
    productChangePrice(@PathVariable Long productId,
                       @Valid @RequestBody ProductChangePriceRequest request) {
        return productService.changeProductPrice(productId, request)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to change product price.")));
    }

    @PutMapping("{productId}/change-stock-level")
    public ResponseEntity<ProductAdminResponse>
    productChangeStockLevel(@PathVariable Long productId,
                            @Valid @RequestBody ProductChangeStockLevelRequest request) {
        return productService.changeProductStockLevel(productId, request)
                .map(product -> ResponseEntity.ok().body(
                        new ProductAdminResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                                                    "Product not found",
                                                    List.of("Failed to change product stock level.")));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void>
    productDelete(@PathVariable Long productId) {
        if (productService.deleteProduct(productId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{productId}/add-to-favorites")
    public ResponseEntity<ProductResponse>
    favoritesAdd(@PathVariable Long productId,
                 @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;

        if (user == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED,
                                   "Not authorized",
                                   List.of("Login user may add product to favorites."));
        }

        return productService.addFavoriteItem(user.getId(), productId)
                .map(product -> ResponseEntity.ok().body(new ProductResponse(product)))
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT,
                                                    "Favorite item addition failure",
                                                    List.of("Failed to create a new favorite item.")));
    }

    @DeleteMapping("/{productId}/remove-from-favorites")
    public ResponseEntity<ProductResponse>
    favoritesRemove(@PathVariable Long productId,
                    @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;

        if (user == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED,
                                   "Not authorized",
                                   List.of("Login user may add product to favorites."));
        }

        if (productService.removeFavoriteItem(user.getId(), productId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
