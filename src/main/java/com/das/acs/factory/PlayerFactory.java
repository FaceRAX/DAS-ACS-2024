package com.das.acs.factory;

import com.das.acs.model.GameBuilder;
import com.das.acs.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerFactory {
    public Player createPlayer(String username) {
        return new Player(username);
    }
}
