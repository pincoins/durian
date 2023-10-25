package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseDateTime;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import kr.pincoin.durian.shop.dto.CategoryCreateRequest;
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
    @Column(name = "category_id")
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

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private CategoryStatus status;

    public Category(String title,
                    String slug,
                    String description,
                    String subDescription,
                    BigDecimal discountRate,
                    CategoryStatus status) {
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.subDescription = subDescription;
        this.discountRate = discountRate;
        this.status = status;
    }

    public Category(CategoryCreateRequest request) {
        this.title = request.getTitle();
        this.slug = request.getSlug();
        this.description = request.getDescription();
        this.subDescription = request.getSubDescription();
        this.discountRate = request.getDiscountRate();

        this.status = CategoryStatus.NORMAL;
    }

    public Category hide() {
        this.status = CategoryStatus.HIDDEN;
        return this;
    }

    public Category show() {
        this.status = CategoryStatus.NORMAL;
        return this;
    }
}
