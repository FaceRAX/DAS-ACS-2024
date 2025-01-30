package com.das.acs.model.filter;

import com.das.acs.model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameFilterChain {
    private final List<Filter> filters;

    public GameFilterChain() {
        this.filters = new ArrayList<>();
    }

    public GameFilterChain addFilter(Filter filter) {
        filters.add(filter);
        return this;
    }

    public List<Game> execute(List<Game> games) {
        List<Game> filteredGames = games;
        for (Filter filter : filters) {
            filteredGames = filter.apply(filteredGames);
        }
        return filteredGames;
    }
}