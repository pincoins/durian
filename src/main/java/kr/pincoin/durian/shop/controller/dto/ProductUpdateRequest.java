package kr.pincoin.durian.shop.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import kr.pincoin.durian.common.validation.NullOrNotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUpdateRequest {
    @JsonProperty("slug")
    @NullOrNotBlank
    private String slug;

    @JsonProperty("name")
    @NullOrNotBlank
    private String name;

    @JsonProperty("subtitle")
    @NullOrNotBlank
    private String subtitle;

    @JsonProperty("description")
    @NullOrNotBlank
    private String description;

    @JsonProperty("position")
    @DecimalMin(value = "0")
    private Integer position;
}
