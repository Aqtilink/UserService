package com.aqtilink.user_service.controller;


import com.aqtilink.user_service.dto.FriendRequestDTO;
import com.aqtilink.user_service.model.FriendRequest;
import com.aqtilink.user_service.service.FriendRequestService;
import com.aqtilink.user_service.security.SecurityUtils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

// Controller for managing friend requests

@RestController
@RequestMapping("/api/v1/friend-requests")
public class FriendRequestController {

    private final FriendRequestService service;

    public FriendRequestController(FriendRequestService service) {
        this.service = service;
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public FriendRequest send(@RequestParam("receiverClerkId") String receiverClerkId) {
        return service.send(receiverClerkId);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{id}/accept")
    public void accept(@PathVariable UUID id) {
        service.accept(id);
    }

    @PostMapping("/{id}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable UUID id) {
        service.reject(id);
    }

    @DeleteMapping("/{id}") 
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequest(@PathVariable UUID id) {
        service.deleteRequest(id);
    }

    @GetMapping("/pending")
    @ResponseStatus(HttpStatus.OK)
    public List<FriendRequestDTO> getPendingRequests() {
        String clerkId = SecurityUtils.getCurrentClerkId();
        return service.getPendingRequests(clerkId);
    }
    
}
