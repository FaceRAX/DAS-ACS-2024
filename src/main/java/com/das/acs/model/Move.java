package com.das.acs.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Move {
    private String id;
    private String uci;
    private LocalDateTime timestamp;
    private Player player; // Player who made the move

    public Move(String uci, Player player) {
        this.id = UUID.randomUUID().toString();
        this.uci = uci;
        this.player = player;
        this.timestamp = LocalDateTime.now();
    }

    // Getters

    public String getUci() {
        return uci;
    }
}
