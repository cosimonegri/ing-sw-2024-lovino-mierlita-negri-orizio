package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.ActionNotValidException;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.network.message.servertoclient.StarterPhaseEndedMessage;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;

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
            if (controller.getPhase() == GamePhase.OBJECTIVE) {
                controller.notifyAllListeners(new ViewUpdateMessage(controller.getModelView(), "All the players have placed their starter card."));
                controller.notifyAllListeners(new StarterPhaseEndedMessage());
            }
        } catch (ActionNotValidException ignored) {}
    }
}
