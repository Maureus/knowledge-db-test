package com.example.knowledgedbcore.repo;

import com.example.knowledgedbcore.models.Role;
import com.example.knowledgedbcore.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
}
