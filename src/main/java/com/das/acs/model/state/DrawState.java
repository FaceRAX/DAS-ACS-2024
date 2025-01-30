package com.das.acs.model.state;

import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.util.ChessLogic;

public class DrawState implements GameState {
    private final String drawReason;

    public DrawState(String drawReason) {
        this.drawReason = drawReason;
    }

    @Override
    public void handleMove(Game game, Move move, ChessLogic chessLogic) {
        throw new IllegalStateException("Game over: Draw (" + drawReason + ").");
    }

    @Override
    public String getStateName() {
        return "DRAW_" + drawReason;
    }
}