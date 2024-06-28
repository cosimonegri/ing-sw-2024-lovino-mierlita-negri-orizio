package it.polimi.ingsw.network.message.servertoclient;

/**
 * Ping message sent ot the client.
 */
public class PingRequest implements ServerToClientMessage {
    /**
     * Username of the client.
     */
    private final String username;

    public PingRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
