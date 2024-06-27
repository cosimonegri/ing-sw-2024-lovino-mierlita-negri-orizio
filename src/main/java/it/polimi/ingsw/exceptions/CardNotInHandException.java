package it.polimi.ingsw.exceptions;

/**
 * When the card is not in the player's hand
 */
public class CardNotInHandException extends Exception {
    public CardNotInHandException() {
        super("You don't have this card in your hand.");
    }
}
