package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.*;

@Entity
@Table(name = "shop_category_tree_path")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CategoryTreePath extends BaseDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_tree_path_id")
    private Long id;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "ancestor_id")
    private Category ancestor;

    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "descendant_id")
    private Category descendant;

    @Column(name = "path_length")
    @NotNull
    private Integer pathLength;

    @Column(name = "position")
    private Integer position;

    public static CategoryTreePathBuilder builder(Category ancestor,
                                                  Category descendant,
                                                  Integer pathLength) {
        return new CategoryTreePathBuilder()
                .ancestor(ancestor)
                .descendant(descendant)
                .pathLength(pathLength)
                .position(0);
    }
}
