package com.das.acs.controller;

import com.das.acs.model.Game;
import com.das.acs.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games/{gameId}/board")
public class BoardController {

    @Autowired
    private GameService gameService;

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String getBoardHTML(@PathVariable String gameId) {
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