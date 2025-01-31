package com.das.acs.model.dto;

public class AnalysisResponse {
    private String operationId;

    public AnalysisResponse(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
