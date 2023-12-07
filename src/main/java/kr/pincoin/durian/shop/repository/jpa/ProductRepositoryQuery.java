package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import kr.pincoin.durian.shop.domain.conveter.ProductStockStatus;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryQuery {
    List<Product> findProducts(Long categoryId,
                               String categorySlug,
                               String slug,
                               ProductStatus status,
                               ProductStockStatus stock,
                               Boolean removed);

    List<Product> findProducts(List<Long> ids,
                               ProductStatus status,
                               ProductStockStatus stock,
                               Boolean removed);

    Optional<Product> findProduct(Long id,
                                  Long categoryId,
                                  String slug,
                                  ProductStatus status,
                                  ProductStockStatus stock,
                                  Boolean removed);
}
