package com.das.acs.controller;

import com.das.acs.model.ChessFacade;
import com.das.acs.model.dto.AnalysisRequest;
import com.das.acs.model.dto.AnalysisResponse;
import com.das.acs.model.dto.AnalysisResult;
import com.das.acs.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final ChessFacade chessFacade;

    @Autowired
    public AnalysisController(ChessFacade chessFacade) {
        this.chessFacade = chessFacade;
    }

    @PostMapping
    public ResponseEntity<AnalysisResponse> startAnalysis(
            @RequestBody AnalysisRequest request
    ) {
        AnalysisResponse result = chessFacade.startAnalysis(
                request.getFen(),
                request.getDepth()
        );
        return ResponseEntity.accepted()
                .body(result);
    }

    @GetMapping("/{operationId}")
    public ResponseEntity<AnalysisResult> getAnalysisResult(
            @PathVariable String operationId
    ) {
        AnalysisResult result = chessFacade.getAnalysisResult(operationId);
        return result.getStatus().equals("COMPLETED") ?
                ResponseEntity.ok(result) :
                ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
    }
}