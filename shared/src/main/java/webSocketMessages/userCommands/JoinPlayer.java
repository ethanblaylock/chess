package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
    private ChessGame.TeamColor playerColor;
    private String username;
    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, String username) {
        super(authToken, gameID);
        super.commandType = CommandType.JOIN_PLAYER;
        this.playerColor = playerColor;
        this.username = username;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public String getUsername() { return username; }
}
