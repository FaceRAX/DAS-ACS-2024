package com.das.acs.controller;

import com.das.acs.model.ChessFacade;
import com.das.acs.model.Player;
import com.das.acs.model.dto.PlayerRegistrationRequest;
import com.das.acs.model.dto.StatisticsResponse;
import com.das.acs.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final ChessFacade chessFacade;

    @Autowired
    public PlayerController(ChessFacade chessFacade) {
        this.chessFacade = chessFacade;
    }

    @PostMapping
    public Player registerPlayer(@RequestBody PlayerRegistrationRequest request) {
        return chessFacade.registerPlayer(request.getUsername());
    }

    @GetMapping("/{playerId}")
    public Player getPlayer(@PathVariable String playerId
    ) {
        return chessFacade.getPlayer(playerId).isPresent() ?
                chessFacade.getPlayer(playerId).get() :
                null;
    }

    @GetMapping("/{playerId}/statistics")
    public StatisticsResponse getPlayerStats(@PathVariable String playerId
    ) {
        return chessFacade.getPlayer(playerId).isPresent() ?
                new StatisticsResponse(chessFacade.getPlayer(playerId).get()) :
                null;
    }

    @GetMapping
    public List<Player> listPlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int limit
    ) {
        return chessFacade.listPlayers(page, limit);
    }
}