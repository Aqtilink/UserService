package com.aqtilink.user_service.dto;

public class UserSummaryDTO {
    private String clerkId;
    private String firstName;
    private String lastName;
    private String email;

    public UserSummaryDTO() {}

    public UserSummaryDTO(String clerkId, String firstName, String lastName, String email) {
        this.clerkId = clerkId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getClerkId() {return clerkId;}
    public void setClerkId(String clerkId) {this.clerkId = clerkId;}
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}
