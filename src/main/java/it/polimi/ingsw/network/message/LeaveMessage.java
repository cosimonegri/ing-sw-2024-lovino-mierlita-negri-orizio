package it.polimi.ingsw.network.message;

public class LeaveMessage implements Message{
    private final String username;
    private final int gameId;
    public LeaveMessage(String username, int gameId){
        this.username = username;
        this.gameId = gameId;
    }

    public String getUsername() {
        return username;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    public void execute() {


    }
}
