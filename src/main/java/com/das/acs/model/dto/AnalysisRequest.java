package com.das.acs.model.dto;

public class AnalysisRequest {
    private String fen;      // FEN of the position to analyze
    private int depth = 18;  // default analysis depth

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}