package kr.pincoin.durian.shop.repository.jpa.dto;

import kr.pincoin.durian.shop.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategorySelfParentResult {
    private Category self;

    private Category parent;
}
