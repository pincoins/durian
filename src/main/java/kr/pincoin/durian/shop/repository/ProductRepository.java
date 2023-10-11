package kr.pincoin.durian.shop.repository;

import kr.pincoin.durian.shop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryQuery {
}
