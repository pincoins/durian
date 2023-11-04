package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.shop.controller.dto.ProductChangePriceRequest;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shop_order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
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

    @Embedded
    Price price;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "orderItem",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<OrderItemVoucher> vouchers = new ArrayList<>();

    public static OrderItemBuilder builder(String name,
                                           String subtitle,
                                           String code,
                                           Price price,
                                           Integer quantity) {
        return new OrderItemBuilder()
                .name(name)
                .subtitle(subtitle)
                .code(code)
                .price(price)
                .quantity(quantity)
                .removed(false);
    }

    public OrderItem makeOrder(Order order) {
        if (this.order != null) {
            this.order.getItems().remove(this);
        }

        this.order = order;

        if (!order.getItems().contains(this)) {
            order.getItems().add(this);
        }

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id != null && Objects.equals(id, orderItem.id) && Objects.equals(order, orderItem.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }

    public OrderItem changePrice(ProductChangePriceRequest request) {
        this.price = new Price(request.getListPrice(), request.getSellingPrice(), request.getBuyingPrice());
        return this;
    }

    public OrderItem remove() {
        this.removed = true;
        return this;
    }

    public OrderItem restore() {
        this.removed = false;
        return this;
    }

}
