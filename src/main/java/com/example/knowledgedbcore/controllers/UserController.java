package com.example.knowledgedbcore.controllers;


import com.example.knowledgedbcore.dao.userdao.UserDao;
import com.example.knowledgedbcore.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserDao userService;

    public UserController(UserDao userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User findOne(@PathVariable Integer id) {
        User user = userService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return user;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> findAll() {
        Iterable<User> userIterable = userService.findAll();
        List<User> userList = new ArrayList<>();
        userIterable.forEach(user -> userList.add(user));
        return userList;
    }
}
