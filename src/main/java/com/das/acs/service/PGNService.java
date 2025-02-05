package com.das.acs.service;

import com.das.acs.factory.PlayerFactory;
import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.model.Player;
import com.das.acs.repository.GameRepository;
import com.das.acs.repository.PlayerRepository;
import com.das.acs.util.ChessLogic;
import com.das.acs.util.StockfishUCIAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PGNService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ChessLogic chessLogic;
    private final GameService gameService;

    // Inject dependencies via constructor
    @Autowired
    public PGNService(
            GameRepository gameRepository,
            PlayerRepository playerRepository,
            ChessLogic chessLogic,
            GameService gameService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.chessLogic = chessLogic;
        this.gameService = gameService;
    }

    public Game parsePGN(String pgnText) throws IOException {
        String[] parts = pgnText.split("\r\n\r\n", 2);
        if (parts.length < 2) throw new IllegalArgumentException("Invalid PGN format");

        String metadata = parts[0];
        String movesSan = extractMoves(parts[1]);

        String white = extractTag(metadata, "White");
        String black = extractTag(metadata, "Black");
        Player whitePlayer = playerRepository.findOrCreate(white);
        Player blackPlayer = playerRepository.findOrCreate(black);

        Game game = gameService.createGame(whitePlayer.getId(), blackPlayer.getId());
        gameRepository.save(game);

        String fen = game.getCurrentFEN();
        boolean isWhiteTurn = true;

        for (String sanMove : movesSan.split(" ")) {
            if (sanMove.trim().isEmpty()) continue;

            String uciMove = convertSanToUCI(fen, sanMove);
            fen = chessLogic.applyMoveToFEN(fen, uciMove);

            game.getMoves().add(new Move(uciMove, isWhiteTurn ? whitePlayer : blackPlayer));
            isWhiteTurn = !isWhiteTurn;
        }

        game.setCurrentFEN(fen);
        gameRepository.save(game);
        return game;
    }

    private String extractTag(String metadata, String tag) {
        Pattern pattern = Pattern.compile("\\[" + tag + " \"(.*?)\"\\]");
        Matcher matcher = pattern.matcher(metadata);
        return matcher.find() ? matcher.group(1) : "Unknown";
    }

    private String extractMoves(String moveText) {
        return moveText.replaceAll("\\d+\\.\\.\\.|\\d+\\.", "").replaceAll("\\s+", " ").trim();
    }

    private String convertSanToUCI(String fen, String sanMove) throws IOException {
        List<String> uciMoves = chessLogic.getLegalMoves(fen);

        for (String uciMove : uciMoves) {
            if (sanMatchesUci(fen, sanMove, uciMove)) {
                return uciMove;
            }
        }

        throw new IllegalArgumentException("Invalid SAN move: " + sanMove);
    }

    // 🔹 Check if a UCI move matches the SAN move
    private boolean sanMatchesUci(String fen, String sanMove, String uciMove) {
        String startSquare = uciMove.substring(0, 2);
        String endSquare = uciMove.substring(2, 4);

        // Get piece positions from FEN
        Map<String, String> board = parseFenBoard(fen);

        // Find which piece moved
        String piece = board.get(startSquare);

        if (piece == null) return false; // No piece on the start square

        // Convert SAN notation to UCI move style
        String sanMoveWithoutCapture = sanMove.replace("x", "").replace("+", "").replace("#", "");

        if (sanMoveWithoutCapture.equalsIgnoreCase(piece + endSquare)) {
            return true; // Move matches
        } else if (piece.equals("P") && sanMoveWithoutCapture.equalsIgnoreCase(endSquare)) {
            return true;
        }

        return false;
    }

    // 🔹 Parse FEN board into a map of piece positions
    private Map<String, String> parseFenBoard(String fen) {
        Map<String, String> board = new HashMap<>();
        String[] parts = fen.split(" ");
        String[] rows = parts[0].split("/");

        int rank = 8;
        for (String row : rows) {
            int file = 0;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    file += Character.getNumericValue(c); // Empty squares
                } else {
                    String square = "" + (char) ('a' + file) + rank;
                    board.put(square, String.valueOf(c));
                    file++;
                }
            }
            rank--;
        }
        return board;
    }
}