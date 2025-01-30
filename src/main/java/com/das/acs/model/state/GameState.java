package com.das.acs.model.state;

import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.util.ChessLogic;

public interface GameState {
    void handleMove(Game game, Move move, ChessLogic chessLogic); // Add ChessLogic parameter
    String getStateName();
}
