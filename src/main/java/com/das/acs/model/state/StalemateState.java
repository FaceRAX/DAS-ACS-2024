package com.das.acs.model.state;

import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.util.ChessLogic;

public class StalemateState implements GameState {
    @Override
    public void handleMove(Game game, Move move, ChessLogic chessLogic) {
        throw new IllegalStateException("Game over: Stalemate. No more moves allowed.");
    }

    @Override
    public String getStateName() {
        return "STALEMATE";
    }
}