package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAdminResponse extends ProductResponse {
    @JsonProperty("isRemoved")
    private boolean removed;

    @JsonProperty("buyingPrice")
    private BigDecimal buyingPrice;

    @JsonProperty("minimumStockLevel")
    private Integer minimumStockLevel;

    @JsonProperty("maximumStockLevel")
    private Integer maximumStockLevel;

    public ProductAdminResponse(Product product) {
        super(product);

        this.removed = product.isRemoved();
        this.buyingPrice = product.getPrice().getBuyingPrice();
        this.minimumStockLevel = product.getStockLevel().getMinimumStockLevel();
        this.maximumStockLevel = product.getStockLevel().getMaximumStockLevel();
    }
}
