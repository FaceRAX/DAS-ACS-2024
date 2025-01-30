package com.das.acs.util;

import com.das.acs.model.Game;

public class PGNBuilder {
    private final Game game;

    public PGNBuilder(Game game) {
        this.game = game;
    }

    public String build() {
        StringBuilder pgn = new StringBuilder();
        pgn.append("[Event \"ACS Game\"]\n");
        pgn.append("[White \"").append(game.getPlayerWhite().getUsername()).append("\"]\n");
        pgn.append("[Black \"").append(game.getPlayerBlack().getUsername()).append("\"]\n");
        pgn.append("\n");
        // Add moves in SAN format
        game.getMoves().forEach(move -> pgn.append(move.getUci()).append(" "));
        return pgn.toString();
    }
}
