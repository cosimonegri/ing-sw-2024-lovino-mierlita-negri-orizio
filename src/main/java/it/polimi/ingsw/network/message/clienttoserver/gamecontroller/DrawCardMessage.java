package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.DrawType;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.ActionNotValidException;
import it.polimi.ingsw.exceptions.CardNotOnBoardException;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.network.message.servertoclient.DrawCardAckMessage;
import it.polimi.ingsw.network.message.servertoclient.DrawCardErrorMessage;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;
import it.polimi.ingsw.utilities.Config;

/**
 * Message sent to the server to draw a card.
 */
public class DrawCardMessage extends GameControllerMessage {
    /**
     * Wheter to draw a resource, a gold or a visible card
     */
    private final DrawType type;
    /**
     * The card to be drawn in case the player is drawing a visible card.
     */
    private final PlayableCard card;

    public DrawCardMessage(String username, DrawType type, PlayableCard card){
        super(username);
        this.type = type;
        this.card = card;
    }

    /**
     * Execute the message.
     *
     * @param controller reference to a game controller
     */
    public void execute(GameController controller) {
        try {
            controller.drawCard(this.getUsername(), this.type, this.card);
            controller.notifyListener(this.getUsername(), new DrawCardAckMessage());
            String turnMessage = this.getUsername() + " has finished his turn.";
            String message = controller.getRemainingTurns().isPresent()
                    ? turnMessage + " " + Config.pluralize(controller.getRemainingTurns().get(), "turn") + " before the game ends."
                    : turnMessage;
            controller.notifyAllListeners(new ViewUpdateMessage(controller.getModelView(), message));
        } catch (EmptyDeckException | CardNotOnBoardException e) {
            controller.notifyListener(this.getUsername(), new DrawCardErrorMessage(e.getMessage()));
        } catch (ActionNotValidException ignored) {}
    }
}
