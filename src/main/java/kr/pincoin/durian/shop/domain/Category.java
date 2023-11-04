package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import kr.pincoin.durian.common.domain.BaseAuditor;
import kr.pincoin.durian.shop.controller.dto.CategoryCreateRequest;
import kr.pincoin.durian.shop.controller.dto.CategoryUpdateRequest;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shop_category")
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

    @Column(name = "is_root")
    private boolean isRoot;

    @OneToMany(mappedBy = "ancestor",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<CategoryTreePath> ancestors = new ArrayList<>();

    @OneToMany(mappedBy = "descendant",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<CategoryTreePath> descendants = new ArrayList<>();

    @OneToMany(mappedBy = "category",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

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

    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
        }

        if (product.getCategory() != this) {
            product.changeCategory(this);
        }
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

    public Category update(CategoryUpdateRequest request) {
        if (request.getTitle() != null) {
            this.title = request.getTitle();
        }

        if (request.getSlug() != null) {
            this.slug = request.getSlug();
        }

        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }

        if (request.getSubDescription() != null) {
            this.subDescription = request.getSubDescription();
        }

        if (request.getDiscountRate() != null) {
            this.discountRate = request.getDiscountRate();
        }

        return this;
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
