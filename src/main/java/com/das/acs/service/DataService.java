package com.das.acs.service;

import com.das.acs.factory.PlayerFactory;
import com.das.acs.model.dto.DataExport;
import com.das.acs.repository.GameRepository;
import com.das.acs.repository.PlayerRepository;
import com.das.acs.util.ChessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final ChessLogic chessLogic;

    @Autowired
    public DataService(
            GameRepository gameRepository,
            PlayerRepository playerRepository,
            ChessLogic chessLogic
    ) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.chessLogic = chessLogic;
    }

    public DataExport exportAll() {
        DataExport export = new DataExport();
        export.setPlayers(playerRepository.findAll());
        export.setGames(gameRepository.findAll());
        return export;
    }

    public void importAll(DataExport data) {
        playerRepository.deleteAll();
        gameRepository.deleteAll();
        playerRepository.saveAll(data.getPlayers());
        gameRepository.saveAll(data.getGames(), playerRepository.findAll(), chessLogic);
    }
}