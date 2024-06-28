package it.polimi.ingsw.network.message.clienttoserver;

import it.polimi.ingsw.network.message.Message;

/**
 * Class representing a message that is sent from the client to the server.
 */
public abstract class ClientToServerMessage implements Message {
    /**
     * Username of the client that is sending the message.
     */
    private final String username;

    /**
     * Constructor of the class
     * @param username username of the client that is sending the message
     */
    public ClientToServerMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
