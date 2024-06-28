package it.polimi.ingsw.view;

import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;

/**
 * Interface representing a listener of the view.
 * Classes intereseted in receiving updates from the view can register themselves as listeners in {@link View}.
 */
public interface ViewListener {
    /**
     * @param message the message to update the view listener
     */
    void updateFromView(ClientToServerMessage message);
}
