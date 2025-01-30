package com.das.acs.model.filter;

import com.das.acs.model.Game;

import java.util.List;

public interface Filter {
    List<Game> apply(List<Game> games);
}