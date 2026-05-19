package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

