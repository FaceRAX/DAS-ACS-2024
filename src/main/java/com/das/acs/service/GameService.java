package com.das.acs.service;

import com.das.acs.exceptions.GameStartException;
import com.das.acs.factory.GameFactory;
import com.das.acs.model.Game;
import com.das.acs.model.Player;
import com.das.acs.model.filter.GameFilterChain;
import com.das.acs.model.filter.PlayerFilter;
import com.das.acs.model.filter.StatusFilter;
import com.das.acs.repository.GameRepository;
import com.das.acs.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {
    private final GameFactory gameFactory;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(
            GameFactory gameFactory,
            GameRepository gameRepository,
            PlayerRepository playerRepository
    ) {
        this.gameFactory = gameFactory;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public Game createGame(String player1Id, String player2Id) {
        Optional<Player> player1 = playerRepository.findById(player1Id);
        Optional<Player> player2 = playerRepository.findById(player2Id);
        if (player1.isPresent() && player2.isPresent()) {
            Game game = gameFactory.createNewGame(player1.get(), player2.get());
            gameRepository.save(game);
            return game;
        }
        else{
            throw new GameStartException("Game creation failed");
        }
    }

    public Game getGameById(String gameId) {
        return gameRepository.findById(gameId);
    }

    public List<Game> getFilteredGames(String status, String playerId, int page, int limit) {
        List<Game> games = gameRepository.findAll();
        return new GameFilterChain()
                .addFilter(new StatusFilter(status))
                .addFilter(new PlayerFilter(playerId))
                .execute(games)
                .subList(page * limit, (page + 1) * limit);
    }
}