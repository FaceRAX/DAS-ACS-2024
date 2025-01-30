package com.das.acs.service;

import com.das.acs.factory.GameFactory;
import com.das.acs.factory.PlayerFactory;
import com.das.acs.model.Player;
import com.das.acs.repository.GameRepository;
import com.das.acs.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerFactory playerFactory;
    private final PlayerRepository playerRepository;

    // Inject dependencies via constructor
    @Autowired
    public PlayerService(
            PlayerFactory playerFactory,
            PlayerRepository playerRepository
    ) {
        this.playerFactory = playerFactory;
        this.playerRepository = playerRepository;
    }

    public Player registerPlayer(String username) {
        Player player = playerFactory.createPlayer(username);
        playerRepository.save(player);
        return player;
    }

    public List<Player> getPlayers(int page, int limit) {
        return playerRepository.findAll(page, limit);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(String playerId) {
        return playerRepository.findById(playerId);
    }
}