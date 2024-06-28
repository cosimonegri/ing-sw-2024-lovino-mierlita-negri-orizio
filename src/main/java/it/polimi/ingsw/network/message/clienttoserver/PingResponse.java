package it.polimi.ingsw.network.message.clienttoserver;

/**
 * Ping message sent ot the server.
 */
public class PingResponse extends ClientToServerMessage {
    public PingResponse(String username) {
        super(username);
    }
}
