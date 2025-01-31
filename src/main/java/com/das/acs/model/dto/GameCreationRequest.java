package com.das.acs.model.dto;

public class GameCreationRequest {
    private String player1Id;
    private String player2Id;

    public GameCreationRequest() {}

    public GameCreationRequest(String player1Id, String player2Id) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
    }

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
