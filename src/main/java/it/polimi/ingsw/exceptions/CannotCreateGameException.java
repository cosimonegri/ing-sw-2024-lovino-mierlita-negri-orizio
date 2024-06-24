package it.polimi.ingsw.exceptions;

public class CannotCreateGameException extends Exception {
    public CannotCreateGameException() {
        super("Cannot read JSON files of the cards.");
    }
}
