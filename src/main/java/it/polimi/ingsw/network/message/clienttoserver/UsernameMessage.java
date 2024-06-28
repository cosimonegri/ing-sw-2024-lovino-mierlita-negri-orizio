package it.polimi.ingsw.network.message.clienttoserver;

import it.polimi.ingsw.network.client.ClientInterface;
import it.polimi.ingsw.network.message.clienttoserver.maincontroller.ConnectMessage;

/**
 * Message generated on the client side that should become a {@link ConnectMessage}
 * (by adding a reference to a {@link it.polimi.ingsw.network.client.ClientInterface})
 * before being executed by the server.
 * <br/>
 * The client must send this message once at the beginning of the communication
 * by calling {@link it.polimi.ingsw.network.server.ServerInterface#connectClient(UsernameMessage, ClientInterface)}
 */
public class UsernameMessage extends ClientToServerMessage {
    public UsernameMessage(String username) {
        super(username);
    }
}
