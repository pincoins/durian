package kr.pincoin.durian.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryCreateRequest {
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
}
