package it.polimi.ingsw.network.message;

/**
 * Message generated on the client side that should become a {@link ConnectMessage}
 * (by adding a reference to a {@link it.polimi.ingsw.network.client.ClientInterface})
 * before the server analyzes it
 */
public class UsernameMessage implements Message {
    private final String username;

    public UsernameMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void execute() { }
}
