package com.example.websitebantuonggolumiwood.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepositories extends JpaRepository<ProductsEntity, Integer>, JpaSpecificationExecutor<ProductsEntity> {
    Optional<ProductsEntity> findBySlug(String slug);

    @Query("SELECT DISTINCT p.materials FROM ProductsEntity p WHERE p.materials IS NOT NULL")
    List<String> findDistinctMaterials();
    // lấy product = slug categories
    @Query("SELECT p FROM ProductsEntity p WHERE p.category.slug = :slug")
    List<ProductsEntity> findByCategorySlug(@Param("slug") String slug);
}
