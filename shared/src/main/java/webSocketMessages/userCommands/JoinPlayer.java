package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
    private ChessGame.TeamColor playerColor;

    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken, gameID);
        super.commandType = CommandType.JOIN_PLAYER;
        this.playerColor = playerColor;
    }
}
