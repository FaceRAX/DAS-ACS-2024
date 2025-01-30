package com.das.acs.factory;

import com.das.acs.model.Game;
import com.das.acs.model.GameBuilder;
import com.das.acs.model.Player;
import com.das.acs.util.ChessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameFactory {
    private final ChessLogic chessLogic;

    @Autowired // Inject ChessLogic
    public GameFactory(ChessLogic chessLogic) {
        this.chessLogic = chessLogic;
    }

    public Game createNewGame(Player player1, Player player2) {
        return new GameBuilder()
                .setPlayerWhite(player1)
                .setPlayerBlack(player2)
                .setChessLogic(chessLogic)
                .build();
    }
}
