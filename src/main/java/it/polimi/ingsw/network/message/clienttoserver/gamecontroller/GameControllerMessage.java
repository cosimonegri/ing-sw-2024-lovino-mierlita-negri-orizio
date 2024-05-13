package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;

public abstract class GameControllerMessage extends ClientToServerMessage {
    public GameControllerMessage(String username) {
        super(username);
    }

    public abstract void execute(GameController controller);
}
