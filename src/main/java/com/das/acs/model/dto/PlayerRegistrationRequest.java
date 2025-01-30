package com.das.acs.model.dto;

public class PlayerRegistrationRequest {
    private String username;

    // Default constructor (required for JSON deserialization)
    public PlayerRegistrationRequest() {}

    // Constructor with username
    public PlayerRegistrationRequest(String username) {
        this.username = username;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}