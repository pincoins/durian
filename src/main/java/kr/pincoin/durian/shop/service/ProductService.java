package kr.pincoin.durian.shop.service;

import kr.pincoin.durian.shop.dto.ProductCreateRequest;
import kr.pincoin.durian.shop.dto.ProductResponse;
import kr.pincoin.durian.shop.repository.jpa.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse
    createProduct(ProductCreateRequest request) {


        return new ProductResponse();
    }
}
