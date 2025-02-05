package com.das.acs.model.state;

import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.model.Player;
import com.das.acs.util.ChessLogic;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("TIMEOUT")
public class TimeoutState implements GameState {
    private final Player timedOutPlayer;

    public TimeoutState(Player timedOutPlayer) {
        this.timedOutPlayer = timedOutPlayer;
    }

    @Override
    public void handleMove(Game game, Move move, ChessLogic chessLogic) {
        throw new IllegalStateException("Game over: " + timedOutPlayer.getUsername() + " timed out.");
    }

    @Override
    public String getStateName() {
        return "TIMEOUT";
    }
}