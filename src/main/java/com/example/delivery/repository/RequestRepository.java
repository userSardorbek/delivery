package com.example.delivery.repository;

import com.example.delivery.entity.Product;
import com.example.delivery.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, String> {
    boolean existsByRequestId(String requestId);



    @Query("select r.products from Request r where r.requestId = ?1")
    Set<Product> getProducts(String id);


}
