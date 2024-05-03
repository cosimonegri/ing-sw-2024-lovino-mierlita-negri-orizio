package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.model.deck.card.objectivecard.ObjectiveCard;

public class ChooseObjectiveMessage implements Message{

    private final String username;

    private final ObjectiveCard objective;

    public ChooseObjectiveMessage(String username,ObjectiveCard objective){
        this.username = username;
        this.objective = objective;
    }

    public String getUsername() {
        return username;
    }

    public ObjectiveCard getObjective() {
        return objective;
    }

    @Override
    public void execute() {
        // MainController.getInstance().chooseObjective(this.getUsername(),this.getObjective());
    }
}
