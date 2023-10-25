package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(name = "is_removed")
    private boolean removed;

    @Column(name = "name")
    private String name;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "code")
    private String code;

    @Column(name = "list_price")
    private BigDecimal listPrice;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
