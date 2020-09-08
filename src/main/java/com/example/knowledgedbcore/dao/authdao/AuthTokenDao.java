package com.example.knowledgedbcore.dao.authdao;

import com.example.knowledgedbcore.models.AuthToken;

import java.util.Optional;

public interface AuthTokenDao {
    Optional<AuthToken> findByJwtToken(String jwtToken);

    void delete(AuthToken authToken);

    AuthToken save(AuthToken authToken);
}
