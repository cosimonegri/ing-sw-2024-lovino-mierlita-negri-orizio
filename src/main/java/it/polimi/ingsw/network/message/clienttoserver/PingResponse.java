package it.polimi.ingsw.network.message.clienttoserver;

import it.polimi.ingsw.network.server.Server;

public class PingResponse extends ClientToServerMessage {
    public PingResponse(String username) {
        super(username);
    }

    public void execute() {
        Server.getInstance().pingResponse(this.getUsername());
    }
}
