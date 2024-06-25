package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when a user tries to create a game in which the size of the lobby is not valid
 */
public class PlayersCountNotValidException extends Exception {
    public PlayersCountNotValidException() {
        super("The size of the lobby must be between 2 and 4.");
    }
}