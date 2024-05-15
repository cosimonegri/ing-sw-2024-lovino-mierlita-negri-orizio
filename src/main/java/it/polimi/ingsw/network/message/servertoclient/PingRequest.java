package it.polimi.ingsw.network.message.servertoclient;

public class PingRequest implements ServerToClientMessage {
    private final String username;

    public PingRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
