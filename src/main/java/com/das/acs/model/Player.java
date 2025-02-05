package com.das.acs.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Player {
    private String id;
    private String username;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime registrationDate;
    private int wins;
    private int losses;
    private int draws;

    // Factory Pattern: Use PlayerFactory to create instances
    public Player(String username) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.registrationDate = LocalDateTime.now();
    }

    public Player() {}

    // Statistics method
    public double getWinLossRatio() {
        return (losses == 0) ? wins : (double) wins / losses;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }
}
