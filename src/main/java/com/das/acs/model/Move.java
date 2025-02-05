package com.das.acs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.UUID;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Move {
    private String id;
    private String uci;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;
    private Player player; // Player who made the move

    public Move(String uci, Player player) {
        this.id = UUID.randomUUID().toString();
        this.uci = uci;
        this.player = player;
        this.timestamp = LocalDateTime.now();
    }

    public Move(){}
    // Getters

    public String getUci() {
        return uci;
    }
    public void setUci(String uci) {
        this.uci = uci;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getId() {
        return id;
    }
}
