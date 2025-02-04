package com.das.acs.model;

import com.das.acs.util.ChessLogic;

public class GameBuilder {
    private Player playerWhite;
    private Player playerBlack;
    private ChessLogic chessLogic;

    public GameBuilder setPlayerWhite(Player player) {
        this.playerWhite = player;
        return this;
    }

    public GameBuilder setPlayerBlack(Player player) {
        this.playerBlack = player;
        return this;
    }

    public GameBuilder setChessLogic(ChessLogic chessLogic) {
        this.chessLogic = chessLogic;
        return this;
    }

    public Game build() {
        if (playerWhite == null || playerBlack == null) {
            throw new IllegalStateException("Both players must be set!");
        } else if (chessLogic == null) {
            throw new IllegalStateException("Chess logic not set!");
        }
        return new Game(playerWhite, playerBlack, chessLogic);
    }
}
