package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseAuditor;
import kr.pincoin.durian.shop.controller.dto.CategoryCreateRequest;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Category extends BaseAuditor {
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

    @Transient
    private final List<Category> children = new ArrayList<>();
    @Transient
    private Category parent;

    public static CategoryBuilder builder(String title,
                                          String slug,
                                          String description,
                                          String subDescription,
                                          BigDecimal discountRate,
                                          CategoryStatus status) {
        return new CategoryBuilder()
                .title(title)
                .slug(slug)
                .description(description)
                .subDescription(subDescription)
                .discountRate(discountRate)
                .status(status);
    }

    public static CategoryBuilder builder(CategoryCreateRequest request) {
        return new CategoryBuilder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .description(request.getDescription())
                .subDescription(request.getSubDescription())
                .discountRate(request.getDiscountRate())
                .status(CategoryStatus.NORMAL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id != null
                && Objects.equals(id, category.id)
                && Objects.equals(title, category.title)
                && Objects.equals(slug, category.slug);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, slug);
    }

    public Category hide() {
        this.status = CategoryStatus.HIDDEN;
        return this;
    }

    public Category show() {
        this.status = CategoryStatus.NORMAL;
        return this;
    }

    public void changeParent(Category parent) {
        this.parent = parent;
    }

    public void addChild(Category child) {
        child.changeParent(this);
        this.children.add(child);
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }
}
