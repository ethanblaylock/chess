package webSocketMessages.userCommands;

public class JoinPlayer extends UserGameCommand{
    public JoinPlayer(String authToken) {
        super(authToken);
        commandType = CommandType.JOIN_PLAYER;
    }
}
