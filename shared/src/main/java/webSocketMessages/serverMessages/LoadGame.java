package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{
    private ChessGame.TeamColor game;
    public LoadGame(ChessGame.TeamColor game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame.TeamColor getGame() {return game;}
}
