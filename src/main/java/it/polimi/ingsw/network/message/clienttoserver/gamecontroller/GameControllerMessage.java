package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;

/**
 * Message that is sent from the client to the server, and that needs a {@link GameController} to be executed.
 */
public abstract class GameControllerMessage extends ClientToServerMessage {
    public GameControllerMessage(String username) {
        super(username);
    }

    /**
     * Execute the message.
     *
     * @param controller reference to a game controller
     */
    public abstract void execute(GameController controller);
}
