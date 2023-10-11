package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "sub_description")
    private String subDescription;

    @Column(name = "discount_rate")
    private BigDecimal discountRate;
}
