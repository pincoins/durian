package kr.pincoin.durian.shop.controller;

import kr.pincoin.durian.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
}
