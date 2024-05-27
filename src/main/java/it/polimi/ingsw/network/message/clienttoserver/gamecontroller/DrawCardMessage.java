package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.DrawType;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.CardNotOnBoardException;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.message.servertoclient.DrawCardAckMessage;
import it.polimi.ingsw.network.message.servertoclient.DrawCardErrorMessage;
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
        try {
            Player player = controller.getCurrentPlayer();
            controller.drawCard(this.getUsername(), this.type, this.card);
            controller.notifyListener(this.getUsername(), new DrawCardAckMessage());
            controller.notifyAllListeners(new ViewUpdateMessage(controller.getModelView(), player.getUsername() + " has finished his turn"));
        } catch (EmptyDeckException | CardNotOnBoardException e) {
            controller.notifyListener(this.getUsername(), new DrawCardErrorMessage(e.getMessage()));
        }
    }
}
