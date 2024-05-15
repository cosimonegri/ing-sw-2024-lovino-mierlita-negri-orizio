package it.polimi.ingsw.network.message.clienttoserver;

public class PingResponse extends ClientToServerMessage {
    public PingResponse(String username) {
        super(username);
    }
}
