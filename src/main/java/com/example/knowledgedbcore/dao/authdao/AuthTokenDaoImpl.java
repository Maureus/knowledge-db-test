package com.example.knowledgedbcore.dao.authdao;

import com.example.knowledgedbcore.models.AuthToken;
import com.example.knowledgedbcore.repo.AuthTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthTokenDaoImpl implements AuthTokenDao {

    private AuthTokenRepo authTokenRepo;

    @Autowired
    public AuthTokenDaoImpl(AuthTokenRepo authTokenRepo) {
        this.authTokenRepo = authTokenRepo;
    }

    @Override
    public Optional<AuthToken> findByJwtToken(String jwtToken) {
        return authTokenRepo.findByJwtToken(jwtToken);
    }

    @Override
    public void delete(AuthToken authToken) {
        authTokenRepo.delete(authToken);
    }

    @Override
    public AuthToken save(AuthToken authToken) {
        AuthToken authTokenDb = authTokenRepo.save(authToken);
        return authTokenDb;
    }
}
