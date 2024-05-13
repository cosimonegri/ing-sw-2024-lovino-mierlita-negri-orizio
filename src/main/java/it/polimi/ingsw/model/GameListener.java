package it.polimi.ingsw.model;

import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;

public interface GameListener {
    void updateFromModel(ServerToClientMessage message);
}
