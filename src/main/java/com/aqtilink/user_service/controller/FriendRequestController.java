package com.aqtilink.user_service.controller;


import com.aqtilink.user_service.dto.FriendRequestDTO;
import com.aqtilink.user_service.model.FriendRequest;
import com.aqtilink.user_service.service.FriendRequestService;
import com.aqtilink.user_service.security.SecurityUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

// Controller for managing friend requests

@RestController
@RequestMapping("/api/v1/friend-requests")
@Tag(name = "Friend Requests", description = "Friend request management endpoints")
public class FriendRequestController {

    private final FriendRequestService service;

    public FriendRequestController(FriendRequestService service) {
        this.service = service;
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Send friend request", description = "Sends a friend request to another user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Friend request sent successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or already friends"),
        @ApiResponse(responseCode = "404", description = "Receiver not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public FriendRequest send(
        @Parameter(description = "Clerk ID of the user to send request to") @RequestParam("receiverClerkId") String receiverClerkId
    ) {
        return service.send(receiverClerkId);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept friend request", description = "Accepts a pending friend request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Friend request accepted successfully"),
        @ApiResponse(responseCode = "404", description = "Friend request not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public void accept(
        @Parameter(description = "ID of the friend request") @PathVariable UUID id
    ) {
        service.accept(id);
    }

    @PostMapping("/{id}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Reject friend request", description = "Rejects a pending friend request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Friend request rejected successfully"),
        @ApiResponse(responseCode = "404", description = "Friend request not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public void reject(
        @Parameter(description = "ID of the friend request") @PathVariable UUID id
    ) {
        service.reject(id);
    }

    @DeleteMapping("/{id}") 
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete friend request", description = "Deletes a friend request (cancels if sender, removes if receiver)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Friend request deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Friend request not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public void deleteRequest(
        @Parameter(description = "ID of the friend request") @PathVariable UUID id
    ) {
        service.deleteRequest(id);
    }

    @GetMapping("/pending")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get pending friend requests", description = "Retrieves all pending friend requests for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pending requests"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public List<FriendRequestDTO> getPendingRequests() {
        String clerkId = SecurityUtils.getCurrentClerkId();
        return service.getPendingRequests(clerkId);
    }
    
}
