package com.spring.test;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntityTest extends JpaRepository<ArticleTest, Long> {

    public ArticleTest findById(int id);

    // public List<ArticleTest> findAll();
}
