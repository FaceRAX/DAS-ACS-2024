package com.das.acs.repository;

import com.das.acs.model.Player;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository // Spring annotation for component scanning
public class PlayerRepository {
    // Thread-safe in-memory storage
    private final Map<String, Player> players = new ConcurrentHashMap<>();

    public Player save(Player player) {
        players.put(player.getId(), player);
        return player;
    }

    public Player findOrCreate(String username) {
        // First try to find by username (case-insensitive)
        Optional<Player> existing = players.values().stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(username))
                .findFirst();

        if (existing.isPresent()) {
            return existing.get();
        }

        // Create new player if not found
        Player newPlayer = new Player(username.trim());
        return save(newPlayer);
    }

    public Optional<Player> findById(String playerId) {
        return Optional.ofNullable(players.get(playerId));
    }

    public List<Player> findAll() {
        return new ArrayList<>(players.values());
    }

    public List<Player> findAll(int page, int limit) {
        return players.values().stream()
                .skip((long) page * limit)
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void delete(String playerId) {
        players.remove(playerId);
    }

    public void deleteAll() {
        players.clear();
    }

    public List<Player> saveAll(List<Player> players) {
        return players.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }
}