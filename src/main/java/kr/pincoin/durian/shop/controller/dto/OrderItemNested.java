package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.OrderItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemNested {
    @JsonProperty("name")
    private String name;

    @JsonProperty("subtitle")
    private String subtitle;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("list_price")
    private BigDecimal listPrice;

    @JsonProperty("selling_price")
    private BigDecimal sellingPrice;

    @JsonProperty("quantity")
    private Integer quantity;

    public OrderItemNested(OrderItem orderItem) {
        this.name = orderItem.getName();
        this.subtitle = orderItem.getSubtitle();
        this.slug = orderItem.getSlug();
        this.listPrice = orderItem.getPrice().getListPrice();
        this.sellingPrice = orderItem.getPrice().getSellingPrice();
        this.quantity = orderItem.getQuantity();
    }
}
