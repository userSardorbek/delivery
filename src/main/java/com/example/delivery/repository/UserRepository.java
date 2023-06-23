package com.example.delivery.repository;

import com.example.delivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
   boolean existsByUsername(String username);

   Optional<User> findByUsername(String username);

}
