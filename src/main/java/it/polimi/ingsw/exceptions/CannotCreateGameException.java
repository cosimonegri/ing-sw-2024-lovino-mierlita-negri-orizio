package it.polimi.ingsw.exceptions;

/**
 * When the json files with the cards cannot be parsed
 */
public class CannotCreateGameException extends Exception {
    public CannotCreateGameException() {
        super("Cannot read JSON files of the cards.");
    }
}
