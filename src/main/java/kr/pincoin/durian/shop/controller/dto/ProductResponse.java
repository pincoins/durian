package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.Product;
import kr.pincoin.durian.shop.domain.conveter.ProductStatus;
import kr.pincoin.durian.shop.domain.conveter.ProductStockStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductResponse {
    @JsonProperty("productId")
    private Long id;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("name")
    private String name;

    @JsonProperty("subtitle")
    private String subtitle;

    @JsonProperty("description")
    private String description;

    @JsonProperty("position")
    private Integer position;

    @JsonProperty("status")
    private ProductStatus status;

    @JsonProperty("stock")
    private ProductStockStatus stock;

    @JsonProperty("listPrice")
    private BigDecimal listPrice;

    @JsonProperty("sellingPrice")
    private BigDecimal sellingPrice;

    @JsonProperty("categoryId")
    private Long categoryId;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.slug = product.getSlug();
        this.name = product.getName();
        this.subtitle = product.getSubtitle();
        this.description = product.getDescription();
        this.position = product.getPosition();
        this.status = product.getStatus();
        this.stock = product.getStock();
        this.listPrice = product.getPrice().getListPrice();
        this.sellingPrice  = product.getPrice().getSellingPrice();
        this.categoryId = product.getCategory().getId();
    }
}
