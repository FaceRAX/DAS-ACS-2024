package com.das.acs.model.filter;

import com.das.acs.model.Game;

import java.util.List;
import java.util.stream.Collectors;

public class StatusFilter implements Filter {
    private final String status;

    public StatusFilter(String status) {
        this.status = status;
    }

    @Override
    public List<Game> apply(List<Game> games) {
        if (status == null || status.isEmpty()) return games;
        return games.stream()
                .filter(game -> game.getState().getStateName().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}