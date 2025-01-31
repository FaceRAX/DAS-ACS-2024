package com.das.acs.repository;

import com.das.acs.exceptions.NotFoundException;
import com.das.acs.model.Game;
import com.das.acs.model.Player;
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

    public List<Game> saveAll(List<Game> games) {
        return games.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }
}