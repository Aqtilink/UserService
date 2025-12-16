package com.aqtilink.user_service.service;

import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User create(User user) {
        return repo.save(user);
    }

    public User get(UUID id) {
        return repo.findById(id).orElseThrow();
    }
    public List<User> getAll() {
        return repo.findAll();
    }
}

