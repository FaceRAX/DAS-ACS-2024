package com.das.acs.controller;

import com.das.acs.model.Player;
import com.das.acs.model.dto.PlayerRegistrationRequest;
import com.das.acs.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping
    public Player registerPlayer(@RequestBody PlayerRegistrationRequest request) {
        return playerService.registerPlayer(request.getUsername());
    }

    @GetMapping
    public List<Player> listPlayers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int limit
    ) {
        if(page == 0 && limit == 0)
        {
            return playerService.getAllPlayers();
        }
        return playerService.getPlayers(page, limit);
    }
}