package com.das.acs.model.state;

import com.das.acs.exceptions.ChessEngineException;
import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.util.ChessLogic;

import java.io.IOException;

public class InProgressState implements GameState {
    @Override
    public void handleMove(Game game, Move move, ChessLogic chessLogic) throws ChessEngineException {
        try {
            String newFEN = chessLogic.applyMoveToFEN(game.getCurrentFEN(), move.getUci());
            game.setCurrentFEN(newFEN);

            game.getMoves().add(move);

            if (chessLogic.isCheckmate(newFEN)) {
                game.setState(new CheckmateState());
            }
        } catch (IOException e) {
            throw new ChessEngineException("Failed to process move: " + move.getUci(), e);
        }
    }

    @Override
    public String getStateName() {
        return "IN_PROGRESS";
    }
}
