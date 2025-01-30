package com.das.acs.model.dto;

public class MoveRequest {
    private String uci;       // Move in UCI
    private String playerId;  // ID of the player making the move

    // Default constructor (required for JSON deserialization)
    public MoveRequest() {}

    // Constructor with fields
    public MoveRequest(String uci, String playerId) {
        this.uci = uci;
        this.playerId = playerId;
    }

    // Getters and setters
    public String getUci() {
        return uci;
    }

    public void setUci(String uci) {
        this.uci = uci;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}