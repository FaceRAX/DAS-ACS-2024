package com.das.acs.controller;

import com.das.acs.model.Game;
import com.das.acs.model.dto.GameCreationRequest;
import com.das.acs.service.GameService;
import com.das.acs.service.PGNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private PGNService pgnService;

    @PostMapping
    public Game createGame(@RequestBody GameCreationRequest request) {
        return gameService.createGame(request.getPlayer1Id(), request.getPlayer2Id());
    }

    @PostMapping("/upload")
    public ResponseEntity<Game> uploadPGN(@RequestBody String pgnText) {
        try {
            Game game = pgnService.parsePGN(pgnText);
            return ResponseEntity.ok(game);
        }catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public List<Game> listGames(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String player,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return gameService.getFilteredGames(status, player, page, limit);
    }
}