package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.common.domain.BaseDateTime;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import kr.pincoin.durian.shop.domain.conveter.ProductStatusConverter;
import kr.pincoin.durian.shop.domain.conveter.StockStatus;
import kr.pincoin.durian.shop.domain.conveter.StockStatusConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_removed")
    private boolean removed;

    @Column(name = "name")
    private String name;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "description")
    private String description;

    @Column(name = "position")
    private Integer position;

    @Column(name = "list_price")
    private BigDecimal listPrice;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "minimum_stock_level")
    private Integer minimumStockLevel;

    @Column(name = "maximum_stock_level")
    private Integer maximumStockLevel;

    @Column(name = "slug")
    private String slug;

    @Column(name = "status")
    @NotNull
    @Convert(converter = ProductStatusConverter.class)
    private ProductStatus status;

    @Column(name = "stock")
    @NotNull
    @Convert(converter = StockStatusConverter.class)
    private StockStatus stock;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
