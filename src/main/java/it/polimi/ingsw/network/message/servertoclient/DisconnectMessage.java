package it.polimi.ingsw.network.message.servertoclient;

public class DisconnectMessage implements ServerToClientMessage {
    private final String username;

    public DisconnectMessage(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username + " left the lobby.";
    }
}
