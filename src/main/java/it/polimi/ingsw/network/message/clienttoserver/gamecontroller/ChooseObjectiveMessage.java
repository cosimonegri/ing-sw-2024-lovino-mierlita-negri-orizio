package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;

public class ChooseObjectiveMessage extends GameControllerMessage {
    private final ObjectiveCard objective;

    public ChooseObjectiveMessage(String username, ObjectiveCard objective){
        super(username);
        this.objective = objective;
    }

    public ObjectiveCard getObjective() {
        return objective;
    }

    @Override
    public void execute(GameController controller) {
        controller.chooseObjective(this.getUsername(), this.objective);
        if (controller.getPhase() == GamePhase.MAIN) {
            System.err.println("Sending update");
            controller.notifyAllListeners(new ViewUpdateMessage(controller.getModelView()));
        }
    }
}
