package com.aqtilink.user_service.controller;

import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.service.UserService;
import com.aqtilink.user_service.dto.FriendDTO;
import com.aqtilink.user_service.security.SecurityUtils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public User get(@PathVariable("id") String clerkId) {
        return service.getByClerkId(clerkId);
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }
    @PutMapping("/{id}")
    public User update(@PathVariable("id") String clerkId, @RequestBody User user) {
        return service.updateByClerkId(clerkId, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String clerkId) {
        service.deleteByClerkId(clerkId);
    }

    @GetMapping("/{id}/email")
    public String getEmail(@PathVariable("id") String clerkId) {
        return service.getEmailByClerkId(clerkId);
    }

    @GetMapping("/{id}/friends")
    public List<FriendDTO> getFriends(@PathVariable("id") String clerkId) {
        return service.getFriendsByClerkId(clerkId);
    }

    @GetMapping("/search")
    public List<FriendDTO> searchUsers(@RequestParam("q") String query) {
        return service.searchUsers(query);
    }

    @GetMapping("/me/friends")
    public List<FriendDTO> getMyFriends() {
        String clerkId = SecurityUtils.getCurrentClerkId();
        return service.getFriendsByClerkId(clerkId);
    }
}

