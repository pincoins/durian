package kr.pincoin.durian.shop.repository.jpa;

import jakarta.persistence.EntityManager;
import kr.pincoin.durian.shop.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CategoryTreePathRepositoryImpl implements CategoryTreePathRepositoryQuery {
    @Autowired
    private final EntityManager em;

    @Override
    public int save(Category category) {
        // JPQL does not support `INSERT INTO SELECT`, but HQL does.
        return em.createQuery(
                        "INSERT INTO" +
                                " CategoryTreePath (ancestor, descendant, pathLength, position, created, modified)" +
                                " SELECT :category, :category, 0, 0, :now, :now")
                .setParameter("category", category)
                .setParameter("now", LocalDateTime.now())
                .executeUpdate();
    }
}
