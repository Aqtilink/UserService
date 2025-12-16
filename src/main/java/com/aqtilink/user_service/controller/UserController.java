package com.aqtilink.user_service.controller;

import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return service.create(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable UUID id) {
        return service.get(id);
    }

    @GetMapping
    public List<User> getAll() {
    return service.getAll();
}
}

