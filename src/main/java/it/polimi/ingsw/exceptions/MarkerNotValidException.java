package it.polimi.ingsw.exceptions;

/**
 * When the marker requested is already taken by another player in the same game
 */
public class MarkerNotValidException extends Exception {
    public MarkerNotValidException() {
        super();
    }
}
