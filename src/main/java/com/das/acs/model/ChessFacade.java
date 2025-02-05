package com.das.acs.model;

import com.das.acs.model.dto.AnalysisResponse;
import com.das.acs.model.dto.AnalysisResult;
import com.das.acs.service.*;
import com.das.acs.util.ChessLogic;
import com.das.acs.util.PGNBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ChessFacade {
    private final GameService gameService;
    private final PlayerService playerService;
    private final MoveService moveService;
    private final AnalysisService analysisService;
    private final ChessLogic chessLogic;
    private final PGNService pgnService;

    @Autowired
    public ChessFacade(
            GameService gameService,
            PlayerService playerService,
            MoveService moveService,
            AnalysisService analysisService,
            ChessLogic chessLogic, PGNService pgnService
    ) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.moveService = moveService;
        this.analysisService = analysisService;
        this.chessLogic = chessLogic;
        this.pgnService = pgnService;
    }

    public Player registerPlayer(String username) {
        return playerService.registerPlayer(username);
    }

    public Game createGame(String player1Id, String player2Id) {
        return gameService.createGame(player1Id, player2Id);
    }

    public Game startGame(String gameId) {
        return gameService.startGame(gameId);
    }

    public String getGameFen(String gameId) {
        return gameService.startGame(gameId).getCurrentFEN();
    }

    public List<Player> listPlayers(int page, int limit
    ) {
        if(page == 0 && limit == 0)
        {
            return playerService.getAllPlayers();
        }
        return playerService.getPlayers(page, limit);
    }

    public Optional<Player> getPlayer(String playerId)
    {
        return playerService.getPlayerById(playerId);
    }

    public List<Game> listGames(String status, String player, int page, int limit) {
        return gameService.getFilteredGames(status, player, page, limit);
    }

    public List<Move> getMoves(String gameId, int page, int limit
    ) {
        return moveService.getMovesForGame(gameId, page, limit);
    }

    public void makeMove(String gameId, String uciMove) {
/*        Game game = gameService.getGameById(gameId);

        if (!chessLogic.validateMove(game.getCurrentFEN(), uciMove)) {
            throw new IllegalArgumentException("Invalid move: " + uciMove);
        }*/

        moveService.addMoveToGame(gameId, uciMove);
    }

    public AnalysisResponse startAnalysis(String fen, int depth) {
        String operationId = analysisService.startAnalysis(
                fen,
                depth
        );
        return new AnalysisResponse(operationId);
    }

    public AnalysisResult getAnalysisResult(String operationId) {
        return analysisService.getAnalysisResult(operationId);
    }

    public String exportGamePGN(String gameId) {
        Game game = gameService.getGameById(gameId);
        return new PGNBuilder(game).build();
    }

    public Game importGamePGN(String pgnText) {
        try {
            return pgnService.parsePGN(pgnText);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getGameVisualization(String gameId) {
        Game game = gameService.getGameById(gameId);
        return renderBoardFromFEN(game.getCurrentFEN());
    }

    private String renderBoardFromFEN(String fen) {
        String[] rows = fen.split(" ")[0].split("/");
        StringBuilder html = new StringBuilder()
                .append("<html><head><style>")
                // Embedded CSS
                .append(".chess-board { border: 2px solid #333; width: 400px; height: 400px; }")
                .append(".chess-row { display: flex; height: 12.5%; }")
                .append(".chess-square { width: 12.5%; display: flex; align-items: center; justify-content: center; font-size: 24px; }")
                .append(".white { background-color: #f0d9b5; }")
                .append(".black { background-color: #b58863; }")
                .append("</style></head><body><div class='chess-board'>");

        for (int i = 0; i < 8; i++) {
            String row = rows[i];
            html.append("<div class='chess-row'>");
            int col = 0;
            for (char c : row.toCharArray()) {
                if (Character.isDigit(c)) {
                    int emptySquares = Character.getNumericValue(c);
                    for (int j = 0; j < emptySquares; j++) {
                        html.append(createSquare(col++, i, ' '));
                    }
                } else {
                    html.append(createSquare(col++, i, c));
                }
            }
            html.append("</div>");
        }
        return html.append("</div></body></html>").toString();
    }

    private String createSquare(int col, int row, char piece) {
        boolean isWhiteSquare = (row + col) % 2 == 0;
        String pieceSymbol = getPieceSymbol(piece);
        return String.format(
                "<div class='chess-square %s'>%s</div>",
                isWhiteSquare ? "white" : "black",
                pieceSymbol
        );
    }

    private String getPieceSymbol(char piece) {
        return switch (Character.toLowerCase(piece)) {
            case 'r' -> "♜"; case 'n' -> "♞"; case 'b' -> "♝";
            case 'q' -> "♛"; case 'k' -> "♚"; case 'p' -> "♟";
            default -> "";
        };
    }
}
