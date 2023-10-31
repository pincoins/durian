package kr.pincoin.durian.shop.repository.jpa;

import kr.pincoin.durian.shop.domain.Category;
import kr.pincoin.durian.shop.domain.CategoryTreePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CategoryTreePathRepository
        extends JpaRepository<CategoryTreePath, Long>, CategoryTreePathRepositoryQuery {
    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "INSERT INTO" +
            " category_tree_path(created, modified, ancestor_id, descendant_id, path_length, position)" +
            " VALUES (now(), now(), ?#{#category.id}, ?#{#category.id}, 0, 0)",
            nativeQuery = true)
    void save(@Param("category") Category category);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "INSERT INTO" +
            " category_tree_path(created, modified, ancestor_id, descendant_id, path_length, position)" +
            " SELECT now(), now(), ctp.ancestor_id, ?#{#child.id}, ctp.path_length + 1, 0" +
            " FROM category_tree_path AS ctp" +
            " WHERE ctp.descendant_id = ?#{#parent.id}",
            nativeQuery = true)
    void save(@Param("parent") Category parent, @Param("child") Category child);
}
