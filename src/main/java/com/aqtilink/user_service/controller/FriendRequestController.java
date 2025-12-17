package com.aqtilink.user_service.controller;


import com.aqtilink.user_service.model.FriendRequest;
import com.aqtilink.user_service.service.FriendRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/friend-requests")
public class FriendRequestController {

    private final FriendRequestService service;

    public FriendRequestController(FriendRequestService service) {
        this.service = service;
    }

    @PostMapping
    public FriendRequest send(@RequestParam UUID senderId, @RequestParam UUID receiverId) {
        return service.send(senderId, receiverId);
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
}
