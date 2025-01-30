package com.das.acs.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StockfishUCIAdapter {
    private Process stockfishProcess;
    private BufferedReader reader;
    private BufferedWriter writer;

    // Start Stockfish engine
    public void startEngine(String enginePath) throws IOException {
        stockfishProcess = new ProcessBuilder(enginePath).start();
        reader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(stockfishProcess.getOutputStream()));
        sendCommand("uci");
        waitForResponse("uciok"); // Wait until Stockfish is ready
    }

    // Send UCI command to Stockfish
    public void sendCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
    }

    public String waitForResponse(String expected) throws IOException {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n"); // Capture ALL lines
            if (line.contains(expected)) {
                break;
            }
        }
        return response.toString();
    }

    public String applyMoveToFEN(String fen, String uciMove) throws IOException {
        sendCommand("position fen " + fen + " moves " + uciMove);
        sendCommand("d");
        String response = waitForResponse("Checkers");

        // Parse FEN from Stockfish's response
        Pattern fenPattern = Pattern.compile("Fen: (\\S+)");
        Matcher matcher = fenPattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IOException("Failed to parse FEN from Stockfish response");
    }

    private String checkBestMove(String fen) throws IOException {
        sendCommand("position fen " + fen);
        sendCommand("go depth 1");
        return waitForResponse("bestmove");
    }

    public boolean validateMove(String fen, String uciMove) throws IOException {
        String bestMove = checkBestMove(fen);
        return bestMove.contains(uciMove);
    }

    public boolean isCheckmate(String fen) throws IOException {
        String bestMove = checkBestMove(fen);
        return bestMove.contains("(none)"); // Stockfish returns "bestmove (none)" for checkmate
    }

    // Get all legal moves for the current position
    public List<String> getLegalMoves(String fen) throws IOException {
        sendCommand("position fen " + fen);
        sendCommand("go perft 1");
        String response = waitForResponse("Nodes searched");
        return parseLegalMoves(response);
    }

    // Parse Stockfish's "perft" output to extract legal moves
    private List<String> parseLegalMoves(String response) {
        List<String> moves = new ArrayList<>();
        String[] lines = response.split("\n");
        for (String line : lines) {
            if (line.contains(":")) {
                String sanMove = line.split(":")[0].trim();
                moves.add(sanMove);
            }
        }
        moves.remove("info string Available processors");
        moves.remove("Nodes searched");
        return moves;
    }

    private List<String> parseUCIMoves(String response) {
        List<String> uciMoves = new ArrayList<>();
        String[] lines = response.split("\n");
        for (String line : lines) {
            if (line.contains(":") && !line.startsWith("Nodes searched")) {
                String uciMove = line.split(":")[0].trim();
                uciMoves.add(uciMove);
            }
        }
        return uciMoves;
    }

    public String analyzePosition(String fen, int depth) throws IOException {
        sendCommand("position fen " + fen);
        sendCommand("go depth " + depth);
        return waitForAnalysisResult();
    }

    private String waitForAnalysisResult() throws IOException {
        StringBuilder analysis = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("bestmove")) {
                analysis.append(line).append("\n");
                break;
            }
            if (line.contains("info")) {
                analysis.append(line).append("\n");
            }
        }
        return analysis.toString();
    }

    // Shut down Stockfish
    public void quit() throws IOException {
        sendCommand("quit");
        stockfishProcess.destroy();
    }
}