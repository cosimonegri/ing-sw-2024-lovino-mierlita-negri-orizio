package it.polimi.ingsw.network.message;

import it.polimi.ingsw.controller.DrawType;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.network.message.clienttoserver.gamecontroller.GameControllerMessage;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;

public class DrawCardMessage extends GameControllerMessage {
    private final DrawType type;
    private final PlayableCard card;

    public DrawCardMessage(String username, DrawType type, PlayableCard card){
        super(username);
        this.type = type;
        this.card = card;
    }

    public DrawType getType() {
        return type;
    }

    public PlayableCard getCard() {
        return card;
    }

    public void execute(GameController controller) {
        controller.drawCard(this.getUsername(), this.type, this.card);
        controller.notifyAllListeners(new ViewUpdateMessage(controller.getModelView()));
    }
}
