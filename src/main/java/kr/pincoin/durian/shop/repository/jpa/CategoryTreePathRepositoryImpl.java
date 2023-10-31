package kr.pincoin.durian.shop.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.pincoin.durian.shop.domain.Category;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CategoryTreePathRepositoryImpl implements CategoryTreePathRepositoryQuery {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public int save(Category category) {
        // @Transactional
        // @Modifying(clearAutomatically = true)
        // @Query(value = "INSERT INTO" +
        //            " category_tree_path(created, modified, ancestor_id, descendant_id, path_length, position)" +
        //            " VALUES (NOW(), NOW(), ?#{#category.id}, ?#{#category.id}, 0, 0)",
        //            nativeQuery = true)
        // void save(@Param("category") Category category);

        return em.createQuery(
                        "INSERT INTO" +
                                " CategoryTreePath (ancestor, descendant, pathLength, position, created, modified)" +
                                " SELECT :category, :category, 0, 0, :now, :now")
                .setParameter("category", category)
                .setParameter("now", LocalDateTime.now())
                .executeUpdate();
    }
}
