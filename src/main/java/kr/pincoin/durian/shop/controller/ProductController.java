package kr.pincoin.durian.shop.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.common.exception.ApiException;
import kr.pincoin.durian.shop.controller.dto.ProductAdminResponse;
import kr.pincoin.durian.shop.controller.dto.ProductCreateRequest;
import kr.pincoin.durian.shop.controller.dto.ProductResponse;
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

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<List<? extends ProductResponse>>
    productList(@RequestParam(name = "categoryId", required = false) Long categoryId,
                @RequestParam(name = "slug", required = false) String slug,
                @RequestParam(name = "status", required = false) ProductStatus status,
                @RequestParam(name = "stock", required = false) ProductStockStatus stock,
                @RequestParam(name = "removed", required = false) Boolean removed,
                @AuthenticationPrincipal UserDetails userDetails) {
        boolean isAdmin = userDetails != null
                && userDetails.getAuthorities().stream().anyMatch(role -> Arrays.asList("ROLE_SYSADMIN",
                                                                                        "ROLE_STAFF")
                .contains(role.getAuthority()));

        return ResponseEntity
                .ok()
                .body(productService.listProducts(categoryId, slug, status, stock, removed)
                              .stream()
                              .map(product -> isAdmin ? new ProductAdminResponse(product)
                                      : new ProductResponse(product))
                              .toList());
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
}
