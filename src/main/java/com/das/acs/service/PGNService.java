package com.das.acs.service;

import com.das.acs.factory.PlayerFactory;
import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.model.Player;
import com.das.acs.repository.GameRepository;
import com.das.acs.repository.PlayerRepository;
import com.das.acs.util.StockfishUCIAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PGNService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final StockfishUCIAdapter stockfish;
    private final GameService gameService;

    // Inject dependencies via constructor
    @Autowired
    public PGNService(
            GameRepository gameRepository,
            PlayerRepository playerRepository,
            StockfishUCIAdapter stockfish,

            GameService gameService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.stockfish = stockfish;
        this.gameService = gameService;
    }

    public Game parsePGN(String pgnText) throws IOException {
        String[] parts = pgnText.split("\n\n");
        String metadata = parts[0];
        String movesSan = parts[1].replaceAll("\\d+\\.", "").trim(); // Remove move numbers

        String white = extractTag(metadata, "White");
        String black = extractTag(metadata, "Black");
        Player whitePlayer = playerRepository.findOrCreate(white);
        Player blackPlayer = playerRepository.findOrCreate(black);

        Game game = gameService.createGame(whitePlayer.getId(), blackPlayer.getId());
        gameRepository.save(game);

        String fen = game.getCurrentFEN();
        for (String sanMove : movesSan.split(" ")) {
            String uciMove = convertSanToUCI(fen, sanMove);
            fen = stockfish.applyMoveToFEN(fen, uciMove);
            game.getMoves().add(new Move(uciMove, whitePlayer)); // Assume white starts
        }

        game.setCurrentFEN(fen);
        gameRepository.save(game);
        return game;
    }

    private String extractTag(String metadata, String tag) {
        Pattern pattern = Pattern.compile("\\[" + tag + " \\\"(.*?)\\\"\\]");
        Matcher matcher = pattern.matcher(metadata);
        return matcher.find() ? matcher.group(1) : "Unknown";
    }

    private String convertSanToUCI(String fen, String sanMove) throws IOException {
        stockfish.sendCommand("position fen " + fen);
        stockfish.sendCommand("go depth 1");
        String legalMoves = stockfish.waitForResponse("bestmove");
        if (legalMoves.contains("bestmove " + sanMove)) {
            return legalMoves.split("bestmove ")[1].split(" ")[0];
        }
        throw new IllegalArgumentException("Invalid SAN move: " + sanMove);
    }
}