package com.das.acs.service;

import com.das.acs.model.dto.AnalysisResult;
import com.das.acs.util.StockfishUCIAdapter;
import com.das.acs.util.UCIEngineAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class AnalysisService {
    private final StockfishUCIAdapter stockfish;
    private final Map<String, AnalysisResult> operations = new ConcurrentHashMap<>();

    @Autowired
    public AnalysisService(StockfishUCIAdapter stockfish) {
        this.stockfish = stockfish;
    }

    public String startAnalysis(String fen, int depth) {
        String operationId = UUID.randomUUID().toString();
        operations.put(operationId, new AnalysisResult("PENDING", null));

        CompletableFuture.runAsync(() -> {
            try {
                String result = stockfish.analyzePosition(fen, depth);
                operations.put(operationId,
                        new AnalysisResult("COMPLETED", result));
            } catch (IOException e) {
                operations.put(operationId,
                        new AnalysisResult("FAILED", e.getMessage()));
            }
        });

        return operationId;
    }

    public AnalysisResult getAnalysisResult(String operationId) {
        return operations.getOrDefault(operationId,
                new AnalysisResult("NOT_FOUND", null));
    }
}