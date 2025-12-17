package com.aqtilink.user_service.service;

import com.aqtilink.user_service.model.FriendRequest;
import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.repository.FriendRequestRepository;
import com.aqtilink.user_service.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.List;

@Service
public class FriendRequestService {
    private final FriendRequestRepository requestRepo;
    private final UserRepository userRepo;

    public FriendRequestService(FriendRequestRepository requestRepo,UserRepository userRepo) {
        this.requestRepo = requestRepo;
        this.userRepo = userRepo;
    }

    public FriendRequest send(UUID senderId, UUID receiverId) {
        if (senderId.equals(receiverId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot send request to yourself"
            );
        }

        if (requestRepo.existsBySenderIdAndReceiverId(senderId, receiverId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Friend request already exists"
            );
        }

        User sender = userRepo.findById(senderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sender not found"));

        User receiver = userRepo.findById(receiverId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Receiver not found"));

        FriendRequest request = new FriendRequest(sender, receiver);
        return requestRepo.save(request);
    }

    public void accept(UUID requestId) {
        FriendRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Request not found"));

        request.accept();
        requestRepo.save(request);
    }

    public void reject(UUID requestId) {
        FriendRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Request not found"));

        request.reject();
        requestRepo.save(request);
    }

    public String getStatus(UUID requestId) {
        FriendRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Request not found"));
        return request.getStatus();
    }

    public List<User> findFriendsOfUser(UUID userId) {
        return requestRepo.findFriendsOfUser(userId);
    }

}
