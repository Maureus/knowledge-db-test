package com.example.knowledgedbcore.dao.userdao;

import com.example.knowledgedbcore.models.User;
import com.example.knowledgedbcore.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDaoImpl implements UserDao {

    private UserRepo userRepo;

    @Autowired
    public UserDaoImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepo.findById(id);
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return userRepo.existsByUsername(userName);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public Optional<User> finUserByUserName(String userName) {
        return userRepo.findUserByUsername(userName);
    }
}
