package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.ActionNotValidException;
import it.polimi.ingsw.exceptions.CardNotInHandException;
import it.polimi.ingsw.exceptions.CoordinatesNotValidException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.network.message.servertoclient.PlayCardAckMessage;
import it.polimi.ingsw.network.message.servertoclient.PlayCardErrorMessage;
import it.polimi.ingsw.network.message.servertoclient.ViewUpdateMessage;
import it.polimi.ingsw.utilities.Config;

public class PlayCardMessage extends GameControllerMessage {
    private final PlayableCard card;

    private final boolean flipped;

    private final Coordinates coords;

    public PlayCardMessage (String username, PlayableCard card, boolean flipped, Coordinates coords) {
        super(username);
        this.card = card;
        this.flipped = flipped;
        this.coords = coords;
    }

    @Override
    public void execute(GameController controller) {
        try {
            controller.playCard(this.getUsername(), this.card, this.flipped, this.coords);
            controller.notifyListener(this.getUsername(), new PlayCardAckMessage());
            // If you are in the last round, your turn ends after you play a card
            if (!controller.isCurrentPlayer(this.getUsername())) {
                String turnMessage = this.getUsername() + " has finished his turn.";
                String message = controller.getPhase() == GamePhase.ENDED
                        ? turnMessage + " The game has ended."
                        : controller.getRemainingTurns().isPresent()
                        ? turnMessage + " " + Config.pluralize(controller.getRemainingTurns().get(), "turn") + " before the game ends."
                        : turnMessage;
                controller.notifyAllListeners(new ViewUpdateMessage(controller.getModelView(), message));
            } else {
                String playMessage = this.getUsername() + " has played a card.";
                controller.notifyAllListeners(new ViewUpdateMessage(controller.getModelView(), playMessage));
            }
        } catch (CardNotInHandException | CoordinatesNotValidException | NotEnoughResourcesException e) {
            controller.notifyListener(this.getUsername(), new PlayCardErrorMessage(e.getMessage()));
        } catch (ActionNotValidException ignored) {}
    }
}
