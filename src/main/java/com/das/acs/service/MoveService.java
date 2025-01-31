package com.das.acs.service;

import com.das.acs.exceptions.ChessEngineException;
import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.model.Player;
import com.das.acs.model.state.CheckmateState;
import com.das.acs.repository.GameRepository;
import com.das.acs.repository.PlayerRepository;
import com.das.acs.util.ChessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MoveService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ChessLogic chessLogic;

    @Autowired
    public MoveService(
            GameRepository gameRepository,
            PlayerRepository playerRepository,
            ChessLogic chessLogic
    ) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.chessLogic = chessLogic;
    }

    public void addMoveToGame(String gameId, String uci, String playerId) {
        Game game = gameRepository.findById(gameId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));

        String currentFEN = game.getCurrentFEN();
        if (!chessLogic.validateMove(currentFEN, uci)) {
            throw new IllegalArgumentException("Invalid move: " + uci);
        }

        try {
            String newFEN = chessLogic.applyMoveToFEN(currentFEN, uci);
            Move move = new Move(uci, player);
            game.getMoves().add(move);
            game.setCurrentFEN(newFEN);
            gameRepository.save(game);

            if (chessLogic.isCheckmate(newFEN)) {
                game.setState(new CheckmateState());
            }
        } catch (IOException e) {
            throw new ChessEngineException("Failed to apply move: " + uci, e);
        }
    }

    public List<Move> getMovesForGame(String gameId, int page, int limit) {
        Game game = gameRepository.findById(gameId);
        List<Move> allMoves = game.getMoves();

        // Calculate pagination bounds
        int start = page * limit;
        int end = Math.min(start + limit, allMoves.size());

        if (start > allMoves.size()) {
            return Collections.emptyList();
        }

        return allMoves.subList(start, end);
    }
}