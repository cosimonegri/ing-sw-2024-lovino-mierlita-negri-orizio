package it.polimi.ingsw.exceptions;

/**
 * When a player tries to draw from an empty deck
 */
public class EmptyDeckException extends Exception {
    public EmptyDeckException() {
        super("You cannot draw a card from an empty deck.");
    }
}
