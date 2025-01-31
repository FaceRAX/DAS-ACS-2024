package com.das.acs.model.dto;

import com.das.acs.model.Game;
import com.das.acs.model.Player;

import java.util.List;

public class DataExport {
    private List<Player> players;
    private List<Game> games;

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}