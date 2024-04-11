package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{

    public Resign(String authToken) {
        super(authToken);
        commandType = CommandType.RESIGN;
    }
}
