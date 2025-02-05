package com.das.acs.model.state;

import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.util.ChessLogic;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("WAITING")
public class WaitingState implements GameState {
    @Override
    public void handleMove(Game game, Move move, ChessLogic chessLogic) {
        throw new IllegalStateException("Game has not started. Waiting for both players.");
    }

    @Override
    public String getStateName() {
        return "WAITING";
    }
}
