package com.aqtilink.user_service.dto;

import java.util.UUID;

public class FriendDTO {
    private UUID id;
    private String firstName;
    private String lastName;

    public FriendDTO(UUID id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}

