package com.das.acs.controller;

import com.das.acs.model.ChessFacade;
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

    private final ChessFacade chessFacade;

    @Autowired
    public GameController(ChessFacade chessFacade) {
        this.chessFacade = chessFacade;
    }


    @PostMapping
    public Game createGame(@RequestBody GameCreationRequest request) {
        return chessFacade.createGame(request.getPlayer1Id(), request.getPlayer2Id());
    }

    @PostMapping("/{gameId}/start")
    public Game startGame(@PathVariable String gameId) {
        return chessFacade.startGame(gameId);
    }

    @GetMapping("/{gameId}/position")
    public String currentPosition(@PathVariable String gameId) {
        return chessFacade.getGameFen(gameId);
    }

    @PostMapping("/upload")
    public ResponseEntity<Game> uploadPGN(@RequestBody String pgnText) {
        try {
            Game game = chessFacade.importGamePGN(pgnText);
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
        return chessFacade.listGames(status, player, page, limit);
    }
}