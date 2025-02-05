package com.das.acs.repository;

import com.das.acs.exceptions.NotFoundException;
import com.das.acs.model.Game;
import com.das.acs.model.Player;
import com.das.acs.model.state.WaitingState;
import com.das.acs.util.ChessLogic;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class GameRepository {
    private final Map<String, Game> games = new ConcurrentHashMap<>();

    public Game save(Game game) {
        games.put(game.getId(), game);
        return game;
    }

    public Game findById(String id) {
        return Optional.ofNullable(games.get(id))
                .orElseThrow(() -> new NotFoundException("Game not found"));
    }

    public List<Game> findAll() {
        return new ArrayList<>(games.values());
    }

    public void deleteAll() {
        games.clear();
    }

    public List<Game> saveAll(List<Game> games, List<Player> players, ChessLogic chessLogic) {
        setPlayerReferences(games, players, chessLogic);
        setGameLogicReferences(games, chessLogic);
        return games.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    private List<Game> setPlayerReferences(List<Game> games, List<Player> players, ChessLogic chessLogic) {

        games.forEach(game -> {
            players.forEach(player -> {
                if(game.getPlayerWhite().getId().equals(player.getId())) {
                    game.setPlayerWhite(player);
                }
                else if(game.getPlayerBlack().getId().equals(player.getId())) {
                    game.setPlayerBlack(player);
                }
            });
            game.getMoves().forEach(move -> {
                players.forEach(player -> {
                    if(move.getPlayer().getId().equals(player.getId())) {
                        move.setPlayer(player);
                    }
                });
            });
        });
        return games;
    }

    private List<Game> setGameLogicReferences(List<Game> games, ChessLogic chessLogic) {
        games.forEach(game -> {
            game.setGameLogic(chessLogic);
            game.setState(new WaitingState());
        });
        return games;
    }
}