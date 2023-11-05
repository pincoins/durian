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

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.title = category.getTitle();
        this.slug = category.getSlug();
        this.description = category.getDescription();
        this.subDescription = category.getSubDescription();
        this.discountRate = category.getDiscountRate();
        this.status = category.getStatus();
    }
}
