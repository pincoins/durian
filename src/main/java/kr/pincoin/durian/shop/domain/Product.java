package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseAuditor;
import kr.pincoin.durian.shop.controller.dto.ProductUpdateRequest;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import kr.pincoin.durian.shop.domain.conveter.ProductStockStatus;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Product extends BaseAuditor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "is_removed")
    private boolean removed;

    @Embedded
    Price price;

    @Column(name = "name")
    private String name;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "description")
    private String description;

    @Column(name = "position")
    private Integer position;

    @Column(name = "slug")
    private String slug;

    @Embedded
    private StockLevel stockLevel;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private ProductStatus status;

    @Column(name = "stock")
    @Enumerated(value = EnumType.STRING)
    private ProductStockStatus stock;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static ProductBuilder builder(String slug,
                                         String name,
                                         String subtitle,
                                         String description,
                                         Integer position,
                                         Price price,
                                         StockLevel stockLevel,
                                         Category category) {
        return new ProductBuilder()
                .slug(slug)
                .name(name)
                .subtitle(subtitle)
                .description(description)
                .position(position)
                .price(price)
                .stockLevel(stockLevel)
                .category(category)
                .status(ProductStatus.DISABLED)
                .stock(ProductStockStatus.IN_STOCK)
                .removed(false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id != null && Objects.equals(id, product.id) && Objects.equals(slug, product.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slug);
    }

    public Product enable() {
        this.status = ProductStatus.ENABLED;
        return this;
    }

    public Product disable() {
        this.status = ProductStatus.DISABLED;
        return this;
    }

    public Product fill() {
        this.stock = ProductStockStatus.IN_STOCK;
        return this;
    }

    public Product makeEmpty() {
        this.stock = ProductStockStatus.SOLD_OUT;
        return this;
    }

    public Product remove() {
        this.removed = true;
        return this;
    }

    public Product restore() {
        this.removed = false;
        return this;
    }

    public Product changePrice(Price price) {
        this.price = price;
        return this;
    }

    public Product changeStockLevel(StockLevel stockLevel) {
        this.stockLevel = stockLevel;
        return this;
    }

    public Product changeCategory(Category category) {
        this.category = category;
        return this;
    }

    public Product update(ProductUpdateRequest request) {
        this.slug = request.getSlug();
        this.name = request.getName();
        this.subtitle = request.getSubtitle();
        this.description = request.getDescription();
        this.position = request.getPosition();
        return this;
    }
}
