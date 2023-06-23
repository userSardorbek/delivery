package com.example.delivery.repository;

import com.example.delivery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsById(String id);
}
