package com.das.acs.model.state;

import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.model.Player;
import com.das.acs.util.ChessLogic;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("RESIGNED")
public class ResignedState implements GameState {
    private final Player resignedPlayer;

    public ResignedState(Player resignedPlayer) {
        this.resignedPlayer = resignedPlayer;
    }

    @Override
    public void handleMove(Game game, Move move, ChessLogic chessLogic) {
        throw new IllegalStateException("Game over: " + resignedPlayer.getUsername() + " resigned.");
    }

    @Override
    public String getStateName() {
        return "RESIGNED";
    }
}