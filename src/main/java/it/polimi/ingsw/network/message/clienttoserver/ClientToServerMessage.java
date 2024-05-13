package it.polimi.ingsw.network.message.clienttoserver;

import it.polimi.ingsw.network.message.Message;

public abstract class ClientToServerMessage implements Message {
    private final String username;

    public ClientToServerMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
