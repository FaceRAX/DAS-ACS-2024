package com.das.acs.model.state;

import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.util.ChessLogic;

public class CheckmateState implements GameState {
    @Override
    public void handleMove(Game game, Move move, ChessLogic chessLogic) {
        throw new IllegalStateException("Game over: Checkmate. No more moves allowed.");
    }

    @Override
    public String getStateName() {
        return "CHECKMATE";
    }
}
