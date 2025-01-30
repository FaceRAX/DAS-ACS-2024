package com.das.acs.model.dto;

public class AnalysisResult {
    private String status; // "IN_PROGRESS", "COMPLETED"
    private String analysis; // e.g., "bestmove e2e4"

    public AnalysisResult(String status, String analysis) {
        this.status = status;
        this.analysis = analysis;
    }
    // Getters and setters

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}