package com.das.acs.model.dto;

public class GameCreationRequest {
    private String player1Id;
    private String player2Id;

    // Default constructor (required for JSON deserialization)
    public GameCreationRequest() {}

    // Constructor with fields
    public GameCreationRequest(String player1Id, String player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
    }

    // Getters and setters
    public String getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(String player1Id) {
        this.player1Id = player1Id;
    }

    public String getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(String player2Id) {
        this.player2Id = player2Id;
    }
}
