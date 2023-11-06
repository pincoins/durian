package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseDateTime;
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
public class OrderItem extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "slug")
    private String slug;

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
                                           String slug,
                                           Price price,
                                           Integer quantity) {
        return new OrderItemBuilder()
                .name(name)
                .subtitle(subtitle)
                .slug(slug)
                .price(price)
                .quantity(quantity);
    }

    public OrderItem belongsTo(Order order) {
        if (this.order != null) {
            this.order.getItems().remove(this);
        }

        this.order = order;

        if (!order.getItems().contains(this)) {
            order.getItems().add(this);
        }

        return this;
    }

    public void add(OrderItemVoucher orderItemVoucher) {
        if (!vouchers.contains(orderItemVoucher)) {
            vouchers.add(orderItemVoucher);
        }

        if (orderItemVoucher.getOrderItem() != this) {
            orderItemVoucher.belongsTo(this);
        }
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

    public OrderItem changeQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
}
