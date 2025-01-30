package com.das.acs.controller;

import com.das.acs.model.Move;
import com.das.acs.model.dto.MoveRequest;
import com.das.acs.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games/{gameId}/moves")
public class MoveController {

    @Autowired
    private MoveService moveService;

    @PostMapping
    public ResponseEntity<Void> addMove(
            @PathVariable String gameId,
            @RequestBody MoveRequest request
    ) {
        moveService.addMoveToGame(gameId, request.getUci(), request.getPlayerId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Move>> getMoves(
            @PathVariable String gameId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<Move> moves = moveService.getMovesForGame(gameId, page, limit);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(moves.size()))
                .body(moves);
    }
}
