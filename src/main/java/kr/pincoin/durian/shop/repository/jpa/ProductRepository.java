package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQuery {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Product p" +
            " SET p.stockQuantity = :#{#product.stockQuantity}" +
            " WHERE p = :product")
    void updateStockQuantity(@Param("product") Product product);
}
