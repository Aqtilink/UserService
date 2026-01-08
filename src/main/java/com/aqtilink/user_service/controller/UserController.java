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

// Controller for managing users

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<FriendDTO> searchUsers(@RequestParam("q") String query) {
        return service.searchUsers(query);
    }

    @GetMapping("/me/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<FriendDTO> getMyFriends() {
        String clerkId = SecurityUtils.getCurrentClerkId();
        if (clerkId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return service.getFriendsByClerkId(clerkId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") String clerkId) {
        return service.getByClerkId(clerkId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<FriendDTO> getFriends(@PathVariable("id") String clerkId) {
        return service.getFriendsByClerkId(clerkId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User update(@PathVariable("id") String clerkId, @RequestBody User user) {
        String currentUserId = SecurityUtils.getCurrentClerkId();
        if (currentUserId != null && !currentUserId.equals(clerkId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own profile");
        }
        return service.updateByClerkId(clerkId, user);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    public List<UserSummaryDTO> getUsersBatch(@RequestBody List<String> clerkIds) {
        return service.getUsersByClerkIds(clerkIds);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String clerkId) {
        service.deleteByClerkId(clerkId);
    }
}

