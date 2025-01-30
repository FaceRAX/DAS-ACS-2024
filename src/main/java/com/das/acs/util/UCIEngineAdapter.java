package com.das.acs.util;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class UCIEngineAdapter {
    private Process engineProcess;
    private BufferedReader reader;
    private BufferedWriter writer;
    private ExecutorService executor;

    public UCIEngineAdapter(String enginePath) throws IOException {
        engineProcess = new ProcessBuilder(enginePath).start();
        reader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(engineProcess.getOutputStream()));
        executor = Executors.newSingleThreadExecutor();

        // Initialize UCI protocol
        sendCommand("uci");
        waitForResponse("uciok");
    }

    // Send UCI command to the engine
    public void sendCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
    }

    // Wait for a specific response (e.g., "uciok")
    private void waitForResponse(String expected) {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(expected)) break;
            }
        } catch (IOException e) {
            throw new RuntimeException("Engine communication failed", e);
        }
    }

    // Validate a move using UCI
    public boolean validateMove(String fen, String sanMove) {
        try {
            sendCommand("position fen " + fen);
            sendCommand("go depth 1");
            String bestMove = readBestMove();
            return bestMove.contains(sanMove);
        } catch (IOException e) {
            return false;
        }
    }

    // Analyze a position (asynchronous)
    public CompletableFuture<String> analyzePosition(String fen, int depth) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                sendCommand("position fen " + fen);
                sendCommand("go depth " + depth);
                return readBestMove();
            } catch (IOException e) {
                return "error";
            }
        }, executor);
    }

    // Read "bestmove" from engine output
    private String readBestMove() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("bestmove")) {
                return line.split(" ")[1]; // e.g., "e2e4"
            }
        }
        return "";
    }

    public void close() {
        try {
            sendCommand("quit");
            executor.shutdown();
            engineProcess.waitFor();
        } catch (Exception e) {
            // Handle cleanup errors
        }
    }
}