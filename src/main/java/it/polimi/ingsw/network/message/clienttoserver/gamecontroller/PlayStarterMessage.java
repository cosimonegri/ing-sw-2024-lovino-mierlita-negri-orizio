package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.ActionNotValidException;

public class PlayStarterMessage extends GameControllerMessage {
    private final boolean flipped;

    public PlayStarterMessage(String username, boolean flipped){
        super(username);
        this.flipped = flipped;
    }

    public boolean isFlipped() {
        return flipped;
    }

    @Override
    public void execute(GameController controller) {
        try {
            controller.playStarter(this.getUsername(), this.flipped);
        } catch (ActionNotValidException ignored) {}
    }
}
