package it.polimi.ingsw.exceptions;

public class EmptyDeckException extends Exception {
    public EmptyDeckException() {
        super("You cannot draw a card from an empty deck.");
    }
}
