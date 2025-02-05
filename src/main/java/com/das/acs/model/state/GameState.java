package com.das.acs.model.state;

import com.das.acs.model.Game;
import com.das.acs.model.Move;
import com.das.acs.util.ChessLogic;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "stateName")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WaitingState.class, name = "WAITING"),
        @JsonSubTypes.Type(value = InProgressState.class, name = "IN_PROGRESS"),
        @JsonSubTypes.Type(value = CheckmateState.class, name = "CHECKMATE"),
        @JsonSubTypes.Type(value = DrawState.class, name = "DRAW"),
        @JsonSubTypes.Type(value = ResignedState.class, name = "RESIGNED"),
        @JsonSubTypes.Type(value = StalemateState.class, name = "STALEMATE"),
        @JsonSubTypes.Type(value = TimeoutState.class, name = "TIMEOUT")
})
public interface GameState {
    void handleMove(Game game, Move move, ChessLogic chessLogic); // Add ChessLogic parameter
    String getStateName();
}
