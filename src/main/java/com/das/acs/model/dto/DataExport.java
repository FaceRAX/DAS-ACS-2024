package com.das.acs.model.dto;

import com.das.acs.model.Game;
import com.das.acs.model.Player;
import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DataExport {

    private List<Player> players;
    private List<Game> games;

    public DataExport() {}

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