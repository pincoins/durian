package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.auth.domain.User;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.*;

@Entity
@Table(name = "shop_favorite_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class FavoriteItem extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_item_id")
    private Long id;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public static FavoriteItemBuilder builder(User user, Product product) {
        return new FavoriteItemBuilder()
                .user(user)
                .product(product);
    }
}
