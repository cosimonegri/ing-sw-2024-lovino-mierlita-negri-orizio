package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.utilities.Config;

/**
 * Exception thrown when the player tries to choose an empty username
 * or a username that contains invalid characters
 */
public class UsernameNotValidException extends Exception {
    public UsernameNotValidException() {
        super(Config.USERNAME_NOT_VALID_MESSAGE);
    }
}
