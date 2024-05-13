package it.polimi.ingsw.network.message.clienttoserver.maincontroller;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.network.message.clienttoserver.ClientToServerMessage;

public abstract class MainControllerMessage extends ClientToServerMessage {
    public MainControllerMessage(String username) {
        super(username);
    }

    public abstract void execute(MainController controller);
}

