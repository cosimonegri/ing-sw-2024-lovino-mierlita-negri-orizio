package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;

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
    }
}
