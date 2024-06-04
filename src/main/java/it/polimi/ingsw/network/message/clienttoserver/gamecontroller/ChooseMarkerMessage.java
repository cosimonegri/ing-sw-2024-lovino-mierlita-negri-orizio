package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.ActionNotValidException;
import it.polimi.ingsw.exceptions.MarkerNotValidException;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.player.Marker;
import it.polimi.ingsw.network.message.servertoclient.ChooseMarkerAckMessage;
import it.polimi.ingsw.network.message.servertoclient.ChooseMarkerErrorMessage;
import it.polimi.ingsw.network.message.servertoclient.StarterPhaseEndedMessage;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;

public class ChooseMarkerMessage extends GameControllerMessage {
    private final Marker marker;

    public ChooseMarkerMessage(String username, Marker marker) {
        super(username);
        this.marker = marker;
    }

    @Override
    public void execute(GameController controller) {
        try {
            controller.chooseMarker(this.getUsername(), this.marker);
            controller.notifyListener(this.getUsername(), new ChooseMarkerAckMessage());
            if (controller.getPhase() == GamePhase.OBJECTIVE) {
                controller.notifyAllListeners(new ViewUpdateMessage(controller.getModelView(), "All the players have placed their starter card."));
                controller.notifyAllListeners(new StarterPhaseEndedMessage());
            }
        } catch (MarkerNotValidException e) {
            controller.notifyListener(this.getUsername(), new ChooseMarkerErrorMessage());
        } catch (ActionNotValidException ignored) {}
    }
}
