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
            // 1. Validate move (already done in MoveService)
            // 2. Update FEN
            String newFEN = chessLogic.applyMoveToFEN(game.getCurrentFEN(), move.getUci());
            game.setCurrentFEN(newFEN);

            // 3. Add the move to the game's move list
            game.getMoves().add(move);

            // 4. Check for game-ending conditions
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
