package com.das.acs.model;

import com.das.acs.model.state.GameState;
import com.das.acs.model.state.InProgressState;
import com.das.acs.model.state.WaitingState;
import com.das.acs.util.ChessLogic;
import com.das.acs.util.PGNBuilder;
import com.fasterxml.jackson.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Game {
    private String id;
    private Player playerWhite;
    private Player playerBlack;
    private List<Move> moves;
    private String currentFEN;
    private GameState state;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime endTime;
    private ChessLogic chessLogic;

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

    public Game(){}

    // Called when both players join
    public Game startGame() {
        if (playerWhite != null && playerBlack != null) {
            setState(new InProgressState());
        }
        return this;
    }

    // Delegate move handling to the current state
    public void addMove(Move move) {
        state.handleMove(this, move, chessLogic);
    }

    public String exportPGN() {
        // Builder Pattern: Use PGNBuilder to generate PGN string
        return new PGNBuilder(this).build();
    }

    public void setGameLogic(ChessLogic chessLogic) {
        this.chessLogic = chessLogic;
    }

    public void setPlayerWhite(Player playerWhite) {
        this.playerWhite = playerWhite;
    }

    public void setPlayerBlack(Player playerBlack) {
        this.playerBlack = playerBlack;
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
    public Player getCurrentPlayerTurn() {
        String[] fenParts = this.currentFEN.split(" ");
        if (fenParts.length < 2) {
            throw new IllegalStateException("Invalid FEN format");
        }

        String activeColor = fenParts[1];
        return activeColor.equalsIgnoreCase("w")
                ? this.playerWhite
                : this.playerBlack;
    }
    public Player getPlayerWhite() { return playerWhite; }
    public Player getPlayerBlack() { return playerBlack; }
    public GameState getState() { return state; }

    public String getId() {
        return id;
    }
}
