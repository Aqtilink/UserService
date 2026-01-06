package com.aqtilink.user_service.controller;

import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.service.UserService;
import com.aqtilink.user_service.dto.FriendDTO;
import com.aqtilink.user_service.security.SecurityUtils;
import com.aqtilink.user_service.dto.UserSummaryDTO;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/search")
    public List<FriendDTO> searchUsers(@RequestParam("q") String query) {
        return service.searchUsers(query);
    }

    @GetMapping("/me/friends")
    public List<FriendDTO> getMyFriends() {
        String clerkId = SecurityUtils.getCurrentClerkId();
        if (clerkId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return service.getFriendsByClerkId(clerkId);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") String clerkId) {
        return service.getByClerkId(clerkId);
    }

    @GetMapping("/{id}/email")
    public String getEmail(@PathVariable("id") String clerkId) {
        return service.getEmailByClerkId(clerkId);
    }

    @GetMapping("/{id}/friends")
    public List<FriendDTO> getFriends(@PathVariable("id") String clerkId) {
        return service.getFriendsByClerkId(clerkId);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable("id") String clerkId, @RequestBody User user) {
        String currentUserId = SecurityUtils.getCurrentClerkId();
        if (currentUserId != null && !currentUserId.equals(clerkId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own profile");
        }
        return service.updateByClerkId(clerkId, user);
    }

    @PostMapping("/batch")
    public List<UserSummaryDTO> getUsersBatch(@RequestBody List<String> clerkIds) {
        return service.getUsersByClerkIds(clerkIds);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String clerkId) {
        service.deleteByClerkId(clerkId);
    }
}

