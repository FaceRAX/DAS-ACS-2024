package com.das.acs.model.dto;

import com.das.acs.model.Player;

import java.time.LocalDateTime;

public class StatisticsResponse {
    private Double winLossRatio;
    private int wins;
    private int losses;
    private int draws;
    private LocalDateTime registationDate;

    public StatisticsResponse(Player player)
    {
        this.winLossRatio = player.getWinLossRatio();
        this.wins = player.getWins();
        this.losses = player.getLosses();
        this.draws = player.getDraws();
        this.registationDate = player.getRegistrationDate();
    }

    public Double getWinLossRatio() {
        return winLossRatio;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public LocalDateTime getRegistationDate() {
        return registationDate;
    }
}
