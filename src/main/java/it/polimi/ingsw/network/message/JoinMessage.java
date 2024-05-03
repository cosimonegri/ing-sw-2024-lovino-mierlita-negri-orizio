package it.polimi.ingsw.network.message;

public class JoinMessage  implements Message{
    private final String username;
    private final int gameId;

    public JoinMessage(String username, int gameId) {
        this.username = username;
        this.gameId = gameId;
    }

    public String getUsername() { return this.username; }

    public int getGameId() { return this.gameId; }
}
