package com.example.knowledgedbcore.services.userservice;

import com.example.knowledgedbcore.models.User;
import com.example.knowledgedbcore.payload.request.LoginRequest;
import com.example.knowledgedbcore.payload.request.SignupRequest;
import com.example.knowledgedbcore.payload.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {

    User createUser(SignupRequest signUpRequest) throws RuntimeException;

    JwtResponse authenticateUser(LoginRequest loginRequest);

    ResponseEntity<?> logoutUser(String authToken) throws IllegalArgumentException;
}
