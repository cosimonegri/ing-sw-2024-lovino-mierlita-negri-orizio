package it.polimi.ingsw.model.exceptions;

/**
 * Exception thrown when the player tries to place a card in the field in a slot that is not valid.
 */
public class CoordinatesAreNotValidException extends RuntimeException {
    public CoordinatesAreNotValidException() {
        super();
    }
}
