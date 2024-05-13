package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;

public interface ViewListener {
    void updateFromView(ClientToServerMessage m);
}
