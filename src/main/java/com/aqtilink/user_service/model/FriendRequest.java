package com.aqtilink.user_service.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "friend_requests", uniqueConstraints = @UniqueConstraint(columnNames = {"sender_id", "receiver_id"}))

public class FriendRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false)
    private String status = "PENDING";

    public FriendRequest() {}

    public FriendRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public void accept() {this.status = "ACCEPTED";}
    public void reject() {this.status = "REJECTED";}

    // getters
    public UUID getId() { return id; }
    public User getSender() { return sender; }
    public User getReceiver() { return receiver; }
    public String getStatus() { return status; }
}

