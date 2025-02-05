package com.das.acs.model.dto;

public class MoveRequest {
    private String uci;       // Move in UCI
//    private String playerId;  // ID of the player making the move

    public MoveRequest() {}

    public MoveRequest(String uci) {
        this.uci = uci;
//        this.playerId = playerId;
    }

    public String getUci() {
        return uci;
    }

    public void setUci(String uci) {
        this.uci = uci;
    }

//    public String getPlayerId() {
//        return playerId;
//    }
//
//    public void setPlayerId(String playerId) {
//        this.playerId = playerId;
//    }
}