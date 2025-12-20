package com.aqtilink.user_service.service;

import com.aqtilink.user_service.model.FriendRequest;
import com.aqtilink.user_service.model.User;
import com.aqtilink.user_service.repository.FriendRequestRepository;
import com.aqtilink.user_service.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.aqtilink.user_service.messaging.NotificationPublisher;
import com.aqtilink.user_service.dto.NotificationEventDTO;

import java.util.UUID;
import java.util.List;

@Service
public class FriendRequestService {
    private final FriendRequestRepository requestRepo;
    private final UserRepository userRepo;
    private final NotificationPublisher notificationService;

    public FriendRequestService(FriendRequestRepository requestRepo,UserRepository userRepo, NotificationPublisher notificationPub) {
        this.requestRepo = requestRepo;
        this.userRepo = userRepo;
        this.notificationService = notificationPub;
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
        requestRepo.save(request);
            NotificationEventDTO notification = new NotificationEventDTO();
            notification.setId(request.getId());
            notification.setEmail(receiver.getEmail());
            notification.setSubject("New Friend Request");
            notification.setMessage("You have a new friend request from " + sender.getFirstName()+ " " + sender.getLastName());
            notificationService.publish(notification);
        return request;
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
    public List<UUID> getAllRequests() {
        return requestRepo.findAllRequests();
    }
    public void deleteRequest(UUID requestId) {
        if (!requestRepo.existsById(requestId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Request not found");
        }
        requestRepo.deleteById(requestId);
    }

}
