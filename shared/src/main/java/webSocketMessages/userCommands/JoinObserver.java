package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    private String username;
    public JoinObserver(String authToken, int gameID, String username) {
        super(authToken, gameID);
        super.commandType = CommandType.JOIN_OBSERVER;
        this.username = username;
    }

    public String getUsername() {return username;}
}
