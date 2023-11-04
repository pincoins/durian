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
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO" +
            " shop_category_tree_path(created, modified, ancestor_id, descendant_id, path_length, position)" +
            " SELECT NOW(), NOW(), ctp.ancestor_id, ?#{#child.id}, ctp.path_length + 1, 0" +
            " FROM shop_category_tree_path AS ctp" +
            " WHERE ctp.descendant_id = ?#{#parent.id}" +
            " UNION ALL" +
            " SELECT NOW(), NOW(), CAST(?#{#child.id} AS INT), CAST(?#{#child.id} AS INT), 0, 0",
            nativeQuery = true)
        // JPA(JPQL/HQL) does not support `UNION ALL`, but native query does.
    void save(@Param("parent") Category parent, @Param("child") Category child);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE ctp" +
            " FROM shop_category_tree_path ctp" +
            " INNER JOIN shop_category_tree_path ctp1" +
            "   ON ctp.ancestor_id = ctp1.ancestor_id AND ctp1.descendant_id = ?#{#category.id}" +
            " INNER JOIN shop_category_tree_path ctp2" +
            "   ON ctp.descendant_id = ctp2.descendant_id AND ctp2.ancestor_id = ?#{#category.id}" +
            " WHERE ctp1.ancestor_id != ctp1.descendant_id",
            nativeQuery = true)
        // JPA(JPQL/HQL) does not support `DELETE FROM INNER JOIN`, but native query does.
    void disconnect(@Param("category") Category category);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO shop_category_tree_path (ancestor_id," +
            "   descendant_id," +
            "   path_length," +
            "   position," +
            "   created," +
            "   modified)" +
            " SELECT supertree.ancestor_id," +
            "   subtree.descendant_id," +
            "   subtree.path_length + supertree.path_length + 1," +
            "   0," +
            "   NOW()," +
            "   NOW()" +
            " FROM shop_category_tree_path AS supertree" +
            " CROSS JOIN shop_category_tree_path AS subtree" +
            " WHERE supertree.descendant_id = ?#{#parent.id} AND subtree.ancestor_id = ?#{#child.id}",
            nativeQuery = true)
        // JPA(JPQL/HQL) does not support `INSERT SELECT CROSS JOIN`, but native query does.
    void connect(@Param("parent") Category parent, @Param("child") Category child);
}
