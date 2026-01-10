package com.aqtilink.user_service.controller;

import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.service.UserService;
import com.aqtilink.user_service.dto.FriendDTO;
import com.aqtilink.user_service.security.SecurityUtils;
import com.aqtilink.user_service.dto.UserSummaryDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Controller for managing users

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search users", description = "Search for users by username or email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    })
    public List<FriendDTO> searchUsers(
        @Parameter(description = "Search query string") @RequestParam("q") String query
    ) {
        return service.searchUsers(query);
    }

    @GetMapping("/me/friends")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get my friends", description = "Retrieves the friend list of the currently authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved friends list"),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public List<FriendDTO> getMyFriends() {
        String clerkId = SecurityUtils.getCurrentClerkId();
        if (clerkId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return service.getFriendsByClerkId(clerkId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by ID", description = "Retrieves a user's profile by their Clerk ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public User getUser(
        @Parameter(description = "Clerk ID of the user") @PathVariable("id") String clerkId
    ) {
        return service.getByClerkId(clerkId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user's friends", description = "Retrieves the friend list of a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved friends list"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public List<FriendDTO> getFriends(
        @Parameter(description = "Clerk ID of the user") @PathVariable("id") String clerkId
    ) {
        return service.getFriendsByClerkId(clerkId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user profile", description = "Updates a user's profile information. Users can only update their own profile.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - can only update own profile"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public User update(
        @Parameter(description = "Clerk ID of the user") @PathVariable("id") String clerkId,
        @RequestBody User user
    ) {
        String currentUserId = SecurityUtils.getCurrentClerkId();
        if (currentUserId != null && !currentUserId.equals(clerkId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own profile");
        }
        return service.updateByClerkId(clerkId, user);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get multiple users", description = "Retrieves user summaries for a batch of Clerk IDs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    })
    public List<UserSummaryDTO> getUsersBatch(@RequestBody List<String> clerkIds) {
        return service.getUsersByClerkIds(clerkIds);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user", description = "Deletes a user account by Clerk ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public void delete(
        @Parameter(description = "Clerk ID of the user to delete") @PathVariable("id") String clerkId
    ) {
        service.deleteByClerkId(clerkId);
    }
}

