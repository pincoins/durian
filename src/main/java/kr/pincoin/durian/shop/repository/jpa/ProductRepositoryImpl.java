package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import kr.pincoin.durian.shop.domain.conveter.ProductStockStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.shop.domain.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements  ProductRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findProducts(Long categoryId,
                                      String categorySlug,
                                      String slug,
                                      ProductStatus status,
                                      ProductStockStatus stock,
                                      Boolean removed) {
        JPAQuery<Product> contentQuery = queryFactory
                .select(product)
                .from(product)
                .innerJoin(product.category)
                .fetchJoin()
                .where(categoryIdEq(categoryId),
                       categorySlugContains(categorySlug),
                       slugContains(slug),
                       statusEq(status),
                       stockEq(stock),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    @Override
    public List<Product> findProducts(List<Long> ids,
                                      ProductStatus status,
                                      ProductStockStatus stock,
                                      Boolean removed) {
        JPAQuery<Product> contentQuery = queryFactory
                .select(product)
                .from(product)
                .innerJoin(product.category)
                .fetchJoin()
                .where(product.id.in(ids),
                       statusEq(status),
                       stockEq(stock),
                       removedEq(removed));

        return contentQuery.fetch();
    }

    @Override
    public Optional<Product> findProduct(Long id,
                                         Long categoryId,
                                         String slug,
                                         ProductStatus status,
                                         ProductStockStatus stock,
                                         Boolean removed) {
        JPAQuery<Product> contentQuery = queryFactory
                .select(product)
                .from(product)
                .innerJoin(product.category)
                .fetchJoin()
                .where(product.id.eq(id),
                       categoryIdEq(categoryId),
                       slugContains(slug),
                       statusEq(status),
                       stockEq(stock),
                       removedEq(removed));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? product.category.id.eq(categoryId) : null;
    }

    BooleanExpression categorySlugContains(String slug) {
        return slug != null && !slug.isBlank() ? product.category.slug.contains(slug) : null;
    }

    BooleanExpression slugContains(String slug) {
        return slug != null && !slug.isBlank() ? product.slug.contains(slug) : null;
    }

    BooleanExpression statusEq(ProductStatus status) {
        return status != null ? product.status.eq(status) : null;
    }

    BooleanExpression stockEq(ProductStockStatus stock) {
        return stock != null ? product.stock.eq(stock) : null;
    }

    BooleanExpression removedEq(Boolean removed) {
        return removed != null ? product.removed.eq(removed) : product.removed.eq(false);
    }
}
