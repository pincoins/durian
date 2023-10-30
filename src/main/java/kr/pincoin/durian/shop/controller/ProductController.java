package kr.pincoin.durian.shop.controller;

import jakarta.validation.Valid;
import kr.pincoin.durian.shop.controller.dto.ProductCreateRequest;
import kr.pincoin.durian.shop.controller.dto.ProductResponse;
import kr.pincoin.durian.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<ProductResponse>
    productCreate(@Valid @RequestBody ProductCreateRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok().body(response);
    }
}
