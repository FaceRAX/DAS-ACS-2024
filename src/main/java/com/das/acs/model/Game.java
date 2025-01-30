package com.das.acs.model;

import com.das.acs.model.state.GameState;
import com.das.acs.model.state.InProgressState;
import com.das.acs.model.state.WaitingState;
import com.das.acs.util.ChessLogic;
import com.das.acs.util.PGNBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private final String id;
    private final Player playerWhite;
    private final Player playerBlack;
    private final List<Move> moves;
    private String currentFEN;
    private GameState state;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;
    private final ChessLogic chessLogic;

    // Builder Pattern: Use GameBuilder to construct
    Game(Player playerWhite, Player playerBlack, ChessLogic chessLogic) {
        this.id = UUID.randomUUID().toString();
        this.playerWhite = playerWhite;
        this.playerBlack = playerBlack;
        this.moves = new ArrayList<>();
        this.currentFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; // Initial FEN
        this.state = new WaitingState();
        this.startTime = LocalDateTime.now();
        this.chessLogic = chessLogic;
    }

    // Called when both players join
    public void startGame() {
        if (playerWhite != null && playerBlack != null) {
            setState(new InProgressState());
        }
    }

    // Delegate move handling to the current state
    public void addMove(Move move) {
        state.handleMove(this, move, chessLogic);
    }

    public String exportPGN() {
        // Builder Pattern: Use PGNBuilder to generate PGN string
        return new PGNBuilder(this).build();
    }

    public String getCurrentFEN() {
        return this.currentFEN;
    }

    public void setCurrentFEN(String fen) {
        this.currentFEN = fen;
    }

    public List<Move> getMoves() {
        return this.moves;
    }

    // State transition method
    public void setState(GameState state) {
        this.state = state;
    }

    // Getters
    public Player getPlayerWhite() { return playerWhite; }
    public Player getPlayerBlack() { return playerBlack; }
    public GameState getState() { return state; }

    public String getId() {
        return id;
    }
}
