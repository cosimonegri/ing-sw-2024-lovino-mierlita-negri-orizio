package it.polimi.ingsw.network.message.clienttoserver.maincontroller;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;

/**
 * Message that is sent from the client to the server, and that needs the {@link MainController} to be executed.
 */
public abstract class MainControllerMessage extends ClientToServerMessage {
    public MainControllerMessage(String username) {
        super(username);
    }

    /**
     * Execute the message.
     *
     * @param controller reference to the main controller
     */
    public abstract void execute(MainController controller);
}

