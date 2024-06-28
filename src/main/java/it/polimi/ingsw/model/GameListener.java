package it.polimi.ingsw.model;

import it.polimi.ingsw.network.message.servertoclient.ServerToClientMessage;

/**
 * Interface representing a listener of the model.
 * Classes intereseted in receiving updates from the model can register themselves as listeners in {@link Game}.
 */
public interface GameListener {
    /**
     * @param message the message to update the model listener
     */
    void updateFromModel(ServerToClientMessage message);
}
