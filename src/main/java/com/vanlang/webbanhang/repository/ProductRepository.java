package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Product> findAll(Pageable pageable);
}
