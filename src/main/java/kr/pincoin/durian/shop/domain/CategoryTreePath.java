package kr.pincoin.durian.shop.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.pincoin.durian.common.domain.BaseDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category_tree_path")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
