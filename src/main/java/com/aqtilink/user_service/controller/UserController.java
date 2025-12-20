package com.aqtilink.user_service.controller;

import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.service.UserService;
import com.aqtilink.user_service.dto.FriendDTO;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    @PutMapping("/{id}")
    public User update(@PathVariable UUID id, @RequestBody User user) {
        return service.update(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @GetMapping("/{id}/email")
    public String getEmail(@PathVariable UUID id) {
        return service.getEmail(id);
    }

    @GetMapping("/{id}/friends")
    public List<FriendDTO> getFriends(@PathVariable UUID id) {
        return service.getFriends(id);
    }
}

