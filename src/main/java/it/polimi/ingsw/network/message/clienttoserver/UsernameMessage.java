package it.polimi.ingsw.network.message.clienttoserver;

import it.polimi.ingsw.network.message.clienttoserver.maincontroller.ConnectMessage;

/**
 * Message generated on the client side that should become a {@link ConnectMessage}
 * (by adding a reference to a {@link it.polimi.ingsw.network.client.ClientInterface})
 * before the server analyzes it
 */
public class UsernameMessage extends ClientToServerMessage {
    public UsernameMessage(String username) {
        super(username);
    }
}
