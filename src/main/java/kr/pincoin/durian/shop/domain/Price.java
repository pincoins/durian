package kr.pincoin.durian.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Price {
    @Column(name = "list_price")
    private BigDecimal listPrice;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @Column(name = "buying_price")
    private BigDecimal buyingPrice;

    @Transient
    private BigDecimal discountRate;

    public Price(BigDecimal listPrice, BigDecimal sellingPrice, BigDecimal buyingPrice) {
        this.listPrice = listPrice;
        this.sellingPrice = sellingPrice;
        this.buyingPrice = buyingPrice;
    }
}
