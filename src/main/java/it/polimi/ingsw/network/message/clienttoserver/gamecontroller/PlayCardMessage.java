package it.polimi.ingsw.network.message.clienttoserver.gamecontroller;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.ActionNotValidException;
import it.polimi.ingsw.exceptions.CardNotInHandException;
import it.polimi.ingsw.exceptions.CoordinatesNotValidException;
import it.polimi.ingsw.exceptions.NotEnoughResourcesException;
import it.polimi.ingsw.model.deck.card.playablecard.PlayableCard;
import it.polimi.ingsw.model.player.Coordinates;
import it.polimi.ingsw.network.message.servertoclient.PlayCardAckMessage;
import it.polimi.ingsw.network.message.servertoclient.PlayCardErrorMessage;

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

    public PlayableCard getCard() {
        return card;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public Coordinates getCoords() {
        return coords;
    }

    @Override
    public void execute(GameController controller) {
        try {
            controller.playCard(this.getUsername(), this.card, this.flipped, this.coords);
            controller.notifyListener(this.getUsername(), new PlayCardAckMessage());
        } catch (CardNotInHandException | CoordinatesNotValidException | NotEnoughResourcesException e) {
            controller.notifyListener(this.getUsername(), new PlayCardErrorMessage(e.getMessage()));
        } catch (ActionNotValidException ignored) {}
    }
}
