package com.das.acs.model.filter;

import com.das.acs.model.Game;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerFilter implements Filter {
    private final String playerId;

    public PlayerFilter(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public List<Game> apply(List<Game> games) {
        if (playerId == null || playerId.isEmpty()) return games;
        return games.stream()
                .filter(game -> isPlayerInGame(game))
                .collect(Collectors.toList());
    }

    private boolean isPlayerInGame(Game game) {
        return game.getPlayerWhite().getId().equals(playerId) ||
                game.getPlayerBlack().getId().equals(playerId);
    }
}