package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when the player tries to choose an empty username
 * or a username that contains invalid characters
 */
public class UsernameNotValidException extends Exception {
    public UsernameNotValidException() {
        super("The username can only contain alphanumeric characters or underscores, and it cannot be empty");
    }
}
