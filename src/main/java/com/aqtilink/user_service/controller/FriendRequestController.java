package com.aqtilink.user_service.controller;


import com.aqtilink.user_service.dto.FriendRequestDTO;
import com.aqtilink.user_service.model.FriendRequest;
import com.aqtilink.user_service.service.FriendRequestService;
import com.aqtilink.user_service.security.SecurityUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/friend-requests")
public class FriendRequestController {

    private final FriendRequestService service;

    public FriendRequestController(FriendRequestService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public FriendRequest send(@RequestParam("receiverClerkId") String receiverClerkId) {
        return service.send(receiverClerkId);
    }

    @PostMapping("/{id}/accept")
    public void accept(@PathVariable UUID id) {
        service.accept(id);
    }

    @PostMapping("/{id}/reject")
    public void reject(@PathVariable UUID id) {
        service.reject(id);
    }
    @GetMapping("/{id}") 
    public String getStatus(@PathVariable UUID id) {
        return service.getStatus(id);
    }
    @GetMapping
    public List<UUID> getAllRequsts() {
        return service.getAllRequests();
    }
    @DeleteMapping("/{id}") 
    public void deleteRequest(@PathVariable UUID id) {
        service.deleteRequest(id);
    }

    @GetMapping("/pending")
    public List<FriendRequestDTO> getPendingRequests() {
        String clerkId = SecurityUtils.getCurrentClerkId();
        return service.getPendingRequests(clerkId);
    }
    
}
