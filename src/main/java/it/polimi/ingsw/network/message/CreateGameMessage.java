package it.polimi.ingsw.network.message;

public class CreateGameMessage implements Message {
    private final String username;
    private final int playersCount;

    public CreateGameMessage(String username, int playersCount) {
        this.username = username;
        this.playersCount = playersCount;
    }

    public String getUsername() { return this.username; }

    public int getPlayersCount() { return this.playersCount; }
}
