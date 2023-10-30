package kr.pincoin.durian.shop.repository.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class CategoryTreePathRepositoryTest {
    @Autowired
    private CategoryTreePathRepository categoryTreePathRepository;

    @PersistenceContext
    private EntityManager em;
}