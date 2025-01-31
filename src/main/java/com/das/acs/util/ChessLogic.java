package com.das.acs.util;

import com.das.acs.exceptions.ChessEngineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ChessLogic {
    private final StockfishUCIAdapter engine;

    @Autowired
    public ChessLogic(StockfishUCIAdapter stockfishAdapter) {
        this.engine = stockfishAdapter;
    }

    public boolean validateMove(String fen, String uciMove) {
        try {
            List<String> legalMoves = engine.getLegalMoves(fen);
            return legalMoves.contains(uciMove);
        } catch (IOException e) {
            throw new ChessEngineException("Failed to validate move", e);
        }
    }

    public String applyMoveToFEN(String fen, String uciMove) throws IOException {
        engine.sendCommand("position fen " + fen + " moves " + uciMove);
        engine.sendCommand("d");
        String response = engine.waitForResponse("Fen:");
        // Extract FEN from response ("Fen: rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1")
        return response.split("Fen: ")[1].trim().split(" ")[0]; // Get only the FEN position
    }

    public boolean isCheckmate(String fen) throws IOException {
        return engine.isCheckmate(fen);
    }

    public void shutdown() throws IOException {
        engine.quit();
    }
}
