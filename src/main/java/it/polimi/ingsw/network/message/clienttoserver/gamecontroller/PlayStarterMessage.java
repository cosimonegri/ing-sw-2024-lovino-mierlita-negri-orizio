package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.ActionNotValidException;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.network.message.servertoclient.StarterPhaseEndedMessage;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;

/**
 * Message sent to the server to play the starter card.
 */
public class PlayStarterMessage extends GameControllerMessage {
    /**
     * Whether to play the starter card flipped or not.
     */
    private final boolean flipped;

    public PlayStarterMessage(String username, boolean flipped){
        super(username);
        this.flipped = flipped;
    }

    /**
     * Execute the message.
     *
     * @param controller reference to a game controller
     */
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
