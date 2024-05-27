package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when the player tries to place a card in the field in a slot that is not valid.
 */
public class CoordinatesNotValidException extends Exception {
    public CoordinatesNotValidException() {
        super("You cannot place a card at these coordinates.");
    }
}
