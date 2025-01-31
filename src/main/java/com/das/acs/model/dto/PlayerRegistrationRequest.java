package com.das.acs.model.dto;

public class PlayerRegistrationRequest {
    private String username;

    public PlayerRegistrationRequest() {}

    public PlayerRegistrationRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}