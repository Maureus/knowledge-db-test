package com.example.knowledgedbcore.dao.userdao;


import com.example.knowledgedbcore.models.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(int id);

    User saveUser(User user);

    Iterable<User> findAll();

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

    Optional<User> finUserByUserName(String userName);
}
