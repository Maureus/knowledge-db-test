package com.example.knowledgedbcore.repo;

import com.example.knowledgedbcore.models.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepo extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByJwtToken(String jwtToken);
}
