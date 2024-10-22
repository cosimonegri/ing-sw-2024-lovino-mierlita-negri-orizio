package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.ActionNotValidException;
import it.polimi.ingsw.exceptions.CardNotInHandException;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;
import it.polimi.ingsw.network.message.servertoclient.ChooseObjectiveAckMessage;
import it.polimi.ingsw.network.message.servertoclient.ChooseObjectiveErrorMessage;
import it.polimi.ingsw.network.message.servertoclient.ObjectivePhaseEndedMessage;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;

/**
 * Message sent to the server to choose the private objective.
 */
public class ChooseObjectiveMessage extends GameControllerMessage {
    /**
     * Private objective choosen.
     */
    private final ObjectiveCard objective;

    public ChooseObjectiveMessage(String username, ObjectiveCard objective){
        super(username);
        this.objective = objective;
    }

    /**
     * Execute the message.
     *
     * @param controller reference to a game controller
     */
    @Override
    public void execute(GameController controller) {
        try {
            controller.chooseObjective(this.getUsername(), this.objective);
            controller.notifyListener(this.getUsername(), new ChooseObjectiveAckMessage());
            if (controller.getPhase() == GamePhase.MAIN) {
                controller.notifyAllListeners(new ViewUpdateMessage(controller.getModelView(), "All the players have choosen their private objective."));
                controller.notifyAllListeners(new ObjectivePhaseEndedMessage());
            }
        } catch (CardNotInHandException e) {
            controller.notifyListener(this.getUsername(), new ChooseObjectiveErrorMessage());
        } catch (ActionNotValidException ignored) {}
    }
}
