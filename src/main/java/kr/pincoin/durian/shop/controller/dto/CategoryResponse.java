package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.conveter.CategoryStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryResponse {
    @JsonProperty("categoryId")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("description")
    private String description;

    @JsonProperty("subDescription")
    private String subDescription;

    @JsonProperty("discountRate")
    private BigDecimal discountRate;

    @JsonProperty("status")
    private CategoryStatus status;

    public CategoryResponse(Long id,
                            String title,
                            String slug,
                            String description,
                            String subDescription,
                            BigDecimal discountRate,
                            CategoryStatus status) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.subDescription = subDescription;
        this.discountRate = discountRate;
        this.status = status;
    }

    public CategoryResponse(Category category) {
        this(category.getId(),
             category.getTitle(),
             category.getSlug(),
             category.getDescription(),
             category.getSubDescription(),
             category.getDiscountRate(),
             category.getStatus());
    }
}
