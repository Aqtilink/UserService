package com.aqtilink.user_service.dto;

import java.util.UUID;

public class FriendRequestDTO {
    private UUID id;
    private String senderId;
    private String senderFirstName;
    private String senderLastName;
    private String senderEmail;
    private String status;

    public FriendRequestDTO() {}

    public FriendRequestDTO(UUID id, String senderId, String senderFirstName, String senderLastName, String senderEmail, String status) {
        this.id = id;
        this.senderId = senderId;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.senderEmail = senderEmail;
        this.status = status;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getSenderFirstName() { return senderFirstName; }
    public void setSenderFirstName(String senderFirstName) { this.senderFirstName = senderFirstName; }
    public String getSenderLastName() { return senderLastName; }
    public void setSenderLastName(String senderLastName) { this.senderLastName = senderLastName; }
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
