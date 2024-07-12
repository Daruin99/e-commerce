package com.finalProject.e_commerce.repository;

import com.finalProject.e_commerce.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    boolean existsByName(String name);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name AND c.id <> :categoryId")
    boolean existsByNameAndNotId(@Param("name") String name,@Param("categoryId") Long categoryId);

}
