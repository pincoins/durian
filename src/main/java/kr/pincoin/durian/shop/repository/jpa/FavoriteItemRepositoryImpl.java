package kr.pincoin.durian.shop.repository.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static kr.pincoin.durian.shop.domain.QFavoriteItem.favoriteItem;
import static kr.pincoin.durian.shop.domain.QProduct.product;


@RequiredArgsConstructor
public class FavoriteItemRepositoryImpl implements FavoriteItemRepositoryQuery {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findItems(Long userId) {
        JPAQuery<Product> contentQuery = queryFactory
                .select(product)
                .from(favoriteItem)
                .innerJoin(favoriteItem.product, product)
                .where(userIdEq(userId),
                       product.status.eq(ProductStatus.ENABLED),
                       product.removed.eq(false));

        return contentQuery.fetch();
    }

    @Override
    public Optional<Product> findItem(Long userId, Long productId) {
        JPAQuery<Product> contentQuery = queryFactory
                .select(product)
                .from(favoriteItem)
                .innerJoin(favoriteItem.product, product)
                .where(userIdEq(userId),
                       productIdEq(productId),
                       product.status.eq(ProductStatus.ENABLED),
                       product.removed.eq(false));

        return Optional.ofNullable(contentQuery.fetchOne());
    }

    BooleanExpression userIdEq(Long userId) {
        return userId != null ? favoriteItem.user.id.eq(userId) : null;
    }

    BooleanExpression productIdEq(Long productId) {
        return productId != null ? favoriteItem.product.id.eq(productId) : null;
    }
}
