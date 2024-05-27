package it.polimi.ingsw.exceptions;

public class CardNotInHandException extends Exception {
    public CardNotInHandException() {
        super("You don't have this card in your hand.");
    }
}
