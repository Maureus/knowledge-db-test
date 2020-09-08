package com.example.knowledgedbcore.controllers;

import com.example.knowledgedbcore.dao.userdao.UserDao;
import com.example.knowledgedbcore.models.User;
import com.example.knowledgedbcore.payload.request.LoginRequest;
import com.example.knowledgedbcore.payload.request.SignupRequest;
import com.example.knowledgedbcore.payload.response.MessageResponse;
import com.example.knowledgedbcore.services.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDao userDao;
    private final UserService userService;

    @Autowired
    public AuthController(UserDao userDao, UserService userService) {
        this.userDao = userDao;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(userService.authenticateUser(loginRequest));
    }

    @PostMapping("/signout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authToken) {

        return userService.logoutUser(authToken);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userDao.existsByUserName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userDao.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }


        User userFromDb = userService.createUser(signUpRequest);
        String location = "http://localhost/api/v1/users/" + userFromDb.getId();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Location", location);

        return ResponseEntity.ok().headers(responseHeaders).build();
    }


}
