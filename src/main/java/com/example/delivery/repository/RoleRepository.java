package com.example.delivery.repository;

import com.example.delivery.entity.Role;
import com.example.delivery.entity.RoleNames;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleNames roleName);
}
