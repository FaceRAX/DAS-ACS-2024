package com.das.acs.controller;

import com.das.acs.model.ChessFacade;
import com.das.acs.model.Game;
import com.das.acs.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games/{gameId}/board")
public class BoardController {

    private final ChessFacade chessFacade;

    @Autowired
    public BoardController(ChessFacade chessFacade) {
        this.chessFacade = chessFacade;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String getBoardHTML(@PathVariable String gameId) {
        return chessFacade.getGameVisualization(gameId);
    }
}